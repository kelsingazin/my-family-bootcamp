package com.family.myfamily.security.services;

import com.family.myfamily.model.entities.DocumentEntity;

import java.util.List;
import java.util.UUID;

public interface DocumentService {

    DocumentEntity save(DocumentEntity documentEntity);

    List<DocumentEntity> getAllDocuments(UUID id);
}
