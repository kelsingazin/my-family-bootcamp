package com.family.myfamily.security.services.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.model.entities.DocumentEntity;
import com.family.myfamily.model.enums.DocumentType;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.repository.DocumentRepository;
import com.family.myfamily.security.services.DocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentEntity save(DocumentEntity documentEntity) {
        log.info("Сохранение документа пользователя в системе");
        setDocumentType(documentEntity);
        checkDocumentExistence(documentEntity);

        return documentRepository.save(documentEntity);
    }

    @Override
    public List<DocumentEntity> getAllDocuments(UUID userId) {
        log.info("Получение всех документов пользователя по userId");
        return documentRepository.findAllByUser_Id(userId);
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
