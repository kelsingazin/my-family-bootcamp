package com.family.myfamily.service.impl;

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
import com.family.myfamily.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final IndividualRepository individualRepository;

    @Transactional
    public DocumentDto save(DocumentDto documentDto) {
        Optional<IndividualEntity> optionalIndividualEntity = individualRepository.findById(documentDto.getIndividual().getId());
        IndividualEntity individual = optionalIndividualEntity.orElseThrow(
                () -> ServiceException.builder()
                        .message("Individual с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build());
        Optional<UserEntity> optionalUserEntity = userRepository.findById(documentDto.getUser().getId());
        UserEntity user = optionalUserEntity.orElseThrow(
                () -> ServiceException.builder()
                        .message("Пользователь с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build());
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
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        UserEntity currentUser = optionalUserEntity.orElseThrow(
                () -> ServiceException.builder()
                        .message("Пользователь с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build());
        if (Objects.equals(contextUser.getPassword(), currentUser.getPassword())) {
            List<DocumentEntity> documents = documentRepository.findAllByUser_IdAndDeletedIsFalse(userId);

            Type listType = new TypeToken<List<DocumentDto>>() {
            }.getType();

            return modelMapper.map(documents, listType);
        } else {
            log.error("Клиент может иметь доступ только к своим документам");
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
            documentRepository.findByPassportSeriesAndDeletedIsFalse(documentEntity.getPassportSeries())
                    .ifPresent(document -> {
                                throw ServiceException.builder()
                                        .errorCode(ErrorCode.ALREADY_EXISTS)
                                        .message("Паспорт с таким номером уже существует")
                                        .build();
                            }
                    );
        }

        if (DocumentType.DRIVER_LICENSE.equals(documentEntity.getDocumentType())) {
            documentRepository.findByLicenseNumberAndDeletedIsFalse(documentEntity.getLicenseNumber())
                    .ifPresent(document -> {
                                throw ServiceException.builder()
                                        .errorCode(ErrorCode.ALREADY_EXISTS)
                                        .message("Водительское удостоверение с таким номером уже существует")
                                        .build();
                            }
                    );
        }
    }

    @Override
    public HttpStatus deleteDocument(UUID documentId) {
        Optional<DocumentEntity> optionalDocumentEntity = documentRepository.findByIdAndDeletedIsFalse(documentId);
        if (optionalDocumentEntity.isPresent()) {
            DocumentEntity document = optionalDocumentEntity.get();
            document.setDeleted(Boolean.TRUE);
            documentRepository.save(document);
            return HttpStatus.OK;
        } else {
            log.error("Документ с таким documentId = {} не существует", documentId);
            throw ServiceException
                    .builder()
                    .errorCode(ErrorCode.NOT_EXISTS)
                    .message("Документ с таким id не существует")
                    .build();
        }
    }
}
