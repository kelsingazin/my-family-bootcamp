package com.family.myfamily.security.services.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.model.dto.DocumentDto;
import com.family.myfamily.model.entities.DocumentEntity;
import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.model.enums.DocumentType;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.repository.DocumentRepository;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.repository.UserRepository;
import com.family.myfamily.security.services.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final IndividualRepository individualRepository;

    public DocumentDto save(DocumentDto documentDto) {
        IndividualEntity individual = individualRepository.findById(documentDto.getIndividual().getId());
        UserEntity user = userRepository.findById(documentDto.getUser().getId());
        log.info("Сохранение документа пользователя {} в системе", individual.getIin());
        DocumentEntity document = modelMapper.map(documentDto, DocumentEntity.class);

        document.setIndividual(individual);
        document.setUser(user);
        setDocumentType(document);
        checkDocumentExistence(document);

        DocumentEntity savedDocument = documentRepository.save(document);

        log.info("Документ пользователя {} сохранен", individual.getIin());
        return modelMapper.map(savedDocument, DocumentDto.class);
    }

    @Override
    public List<DocumentDto> getAllDocuments(UUID userId) {
        log.info("Получение всех документов пользователя по userId");
        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity currentUser = userRepository.findById(userId);
        if (contextUser.getPassword().equals(currentUser.getPassword())) {
            List<DocumentEntity> documents = documentRepository.findAllByUser_Id(userId);

            Type listType = new TypeToken<List<DocumentDto>>() {
            }.getType();

            return modelMapper.map(documents, listType);
        } else {
            throw ServiceException
                    .builder()
                    .message("Клиент может иметь доступ только к своим документам")
                    .errorCode(ErrorCode.AUTH_ERROR)
                    .build();
        }
    }

    @Override
    public DocumentDto getSpecificDocument(UUID userId, DocumentType documentType) {
        List<DocumentDto> documents = getAllDocuments(userId);
        return documents.stream()
                .filter(document -> document.getDocumentType().equals(documentType))
                .findFirst()
                .orElseThrow(() -> ServiceException.builder()
                        .message("У клиента отсутствует документ с таким типом")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build());
    }

    private void setDocumentType(DocumentEntity documentEntity) {
        String passportSeries = documentEntity.getPassportSeries();
        String licenseNumber = documentEntity.getLicenseNumber();

        if ((passportSeries != null && licenseNumber != null) ||
                (passportSeries == null && licenseNumber == null)) {
            throw ServiceException.builder()
                    .errorCode(ErrorCode.INVALID_ARGUMENT)
                    .message("Нет совпадения всех полей одного документа")
                    .build();
        }
        if (passportSeries != null) documentEntity.setDocumentType(DocumentType.PASSPORT);
        if (licenseNumber != null) documentEntity.setDocumentType(DocumentType.DRIVER_LICENSE);
    }

    private void checkDocumentExistence(DocumentEntity documentEntity) {
        log.info("Проверка на существование документа в системе");
        if (DocumentType.PASSPORT.equals(documentEntity.getDocumentType())) {
            documentRepository.findByPassportSeries(documentEntity.getPassportSeries())
                    .ifPresent(document -> {
                                throw ServiceException.builder()
                                        .errorCode(ErrorCode.ALREADY_EXISTS)
                                        .message("Паспорт с таким номером уже существует")
                                        .build();
                            }
                    );
        }

        if (DocumentType.DRIVER_LICENSE.equals(documentEntity.getDocumentType())) {
            documentRepository.findByLicenseNumber(documentEntity.getLicenseNumber())
                    .ifPresent(document -> {
                                throw ServiceException.builder()
                                        .errorCode(ErrorCode.ALREADY_EXISTS)
                                        .message("Водительское удостоверение с таким номером уже существует")
                                        .build();
                            }
                    );
        }
    }
}
