package com.family.myfamily.controller.rest;

import com.family.myfamily.controller.BaseController;
import com.family.myfamily.model.dto.DocumentDto;
import com.family.myfamily.model.entities.DocumentEntity;
import com.family.myfamily.security.services.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController extends BaseController {

    private final ModelMapper modelMapper;
    private final DocumentService documentService;

    //Нет интеграций с ГБД ФЛ и тд. Пока получение документов будет реализовано путем
    //заполнения от АДМИНА
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addDocument(@RequestBody DocumentDto documentDto) {
        log.info("Post request create a document");
        DocumentEntity document = modelMapper.map(documentDto, DocumentEntity.class);
        DocumentDto savedDocument = modelMapper.map(documentService.save(document), DocumentDto.class);
        return buildResponse(savedDocument, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    ResponseEntity<?> getAllDocuments(@PathVariable UUID userId) {
        List<DocumentEntity> allUserDocuments = documentService.getAllDocuments(userId);

        Type listType = new TypeToken<List<DocumentDto>>() {}.getType();
        List<DocumentDto> returnDocuments = modelMapper.map(allUserDocuments, listType);

        return buildResponse(returnDocuments, HttpStatus.OK);
    }
}
