package com.family.myfamily.controller.rest;

import com.family.myfamily.model.dto.DocumentDto;
import com.family.myfamily.security.services.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentService documentService;

    //Нет интеграций с ГБД ФЛ и тд. Пока получение документов будет реализовано путем
    //заполнения от АДМИНА
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    DocumentDto addDocument(@RequestBody DocumentDto documentDto) {
        return documentService.save(documentDto);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    List<DocumentDto> getAllDocuments(@RequestParam UUID userId) {
        return documentService.getAllDocuments(userId);
    }
}
