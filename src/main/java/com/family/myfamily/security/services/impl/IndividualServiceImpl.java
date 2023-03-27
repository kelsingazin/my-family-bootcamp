package com.family.myfamily.security.services.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.model.dto.IndividualDto;
import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.security.services.IndividualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndividualServiceImpl implements IndividualService {

    private final IndividualRepository individualRepository;
    private final ModelMapper modelMapper;

    @Override
    public IndividualDto save(IndividualDto individualDto) {
        IndividualEntity individualDb = individualRepository.findByIin(individualDto.getIin());
        if (individualDb != null) {
            throw ServiceException
                    .builder()
                    .message("Клиент с таким ИИН уже существует")
                    .errorCode(ErrorCode.ALREADY_EXISTS)
                    .build();
        }
        log.info("Сохранение пользователя {} в системе", individualDto.getIin());
        IndividualEntity individual = modelMapper.map(individualDto, IndividualEntity.class);
        IndividualEntity savedIndividual = individualRepository.save(individual);
        log.info("Пользователь {} сохранен в системе", individualDto.getIin());

        return modelMapper.map(savedIndividual, IndividualDto.class);
    }
}
