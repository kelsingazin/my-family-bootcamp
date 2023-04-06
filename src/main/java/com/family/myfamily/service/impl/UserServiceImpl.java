package com.family.myfamily.service.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.model.entities.CardEntity;
import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.payload.response.CardResponse;
import com.family.myfamily.payload.response.UserData;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements com.family.myfamily.service.UserService {

    private final UserRepository userRepository;
    private final IndividualRepository individualRepository;

    @Override
    public UserData getUserData(UUID uuid){
        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity currentUser = userRepository.findById(uuid).orElseThrow(
                () -> ServiceException.builder()
                        .message("Пользователь с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build()
        );
        log.info(contextUser.getPassword());
        if (Objects.equals(contextUser.getPassword(), currentUser.getPassword())) {
            IndividualEntity individual = individualRepository.findByPhoneNumber(currentUser.getPhoneNumber());
            return UserData.builder()
                    .firstName(individual.getFirstName())
                    .lastName(individual.getLastName())
                    .iin(individual.getIin())
                    .status(individual.getMaritalStatus())
                    .cardNumber(currentUser.getCards().get(0).getNumber())
                    .balance(currentUser.getCards().get(0).getBalance())
                    .build();
        } else {
            throw ServiceException
                    .builder()
                    .message("Ошибка валидаций пользователя")
                    .errorCode(ErrorCode.INVALID_ARGUMENT)
                    .build();
        }
    }

}
