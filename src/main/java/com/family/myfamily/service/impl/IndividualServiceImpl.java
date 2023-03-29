package com.family.myfamily.service.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.model.dto.IndividualDto;
import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.repository.UserRepository;
import com.family.myfamily.service.IndividualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndividualServiceImpl implements IndividualService {

    private final IndividualRepository individualRepository;
    private final UserRepository userRepository;
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

    @Override
    public IndividualDto getIndividual(UUID userId) {
        UserEntity userEntity = userRepository.findById(userId).get();
        IndividualEntity individual = individualRepository.findByPhoneNumber(userEntity.getPhoneNumber());
        if (Objects.isNull(individual)) {
            throw ServiceException.builder()
                    .message("Пользователь с таким id не существует")
                    .errorCode(ErrorCode.NOT_EXISTS)
                    .build();
        }

        return modelMapper.map(individual, IndividualDto.class);
    }
}
