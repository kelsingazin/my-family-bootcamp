package com.family.myfamily.service;

import com.family.myfamily.model.dto.DocumentDto;
import com.family.myfamily.model.enums.DocumentType;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public interface DocumentService {

    DocumentDto save(DocumentDto documentDto);

    List<DocumentDto> getAllDocuments(UUID id);

    DocumentDto getSpecificDocument(UUID userId, DocumentType documentType);

    HttpStatus deleteDocument(UUID documentId);
}
