package com.family.myfamily.controller.rest;

import com.family.myfamily.model.dto.IndividualDto;
import com.family.myfamily.security.services.IndividualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/individuals")
@RequiredArgsConstructor
@Slf4j
public class IndividualController {

    private final IndividualService individualService;

    //Нет интеграций с ГБД ФЛ и тд. Пока заведение клиента будет реализовано путем
    //заполнения от АДМИНА
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    IndividualDto addIndividual(@RequestBody IndividualDto individualDto) {
        return individualService.save(individualDto);
    }

}
