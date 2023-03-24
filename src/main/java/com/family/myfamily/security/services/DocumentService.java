package com.family.myfamily.security.services;

import com.family.myfamily.model.dto.DocumentDto;
import com.family.myfamily.model.enums.DocumentType;

import java.util.List;
import java.util.UUID;

public interface DocumentService {

    DocumentDto save(DocumentDto documentDto);

    List<DocumentDto> getAllDocuments(UUID id);

    DocumentDto getSpecificDocument(UUID userId, DocumentType documentType);
}
