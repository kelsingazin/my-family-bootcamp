package com.family.myfamily.service.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.payload.response.UserData;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements com.family.myfamily.service.UserService {

    private final UserRepository userRepository;
    private final IndividualRepository individualRepository;

    private void userValidation(UserEntity currentUser) {
        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!contextUser.getPassword().equals(currentUser.getPassword())) {
            throw ServiceException
                    .builder()
                    .message("Клиент может иметь доступ только к своим запросам")
                    .errorCode(ErrorCode.AUTH_ERROR)
                    .build();
        }
    }

    @Override
    public UserData getUserData(UUID uuid) {
        UserEntity currentUser = userRepository.findById(uuid).orElseThrow(
                () -> ServiceException.builder()
                        .message("Пользователь с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build()
        );

        userValidation(currentUser);
        IndividualEntity individual = individualRepository.findByPhoneNumber(currentUser.getPhoneNumber());

        return UserData.builder()
                .firstName(individual.getFirstName())
                .lastName(individual.getLastName())
                .iin(individual.getIin())
                .status(individual.getMaritalStatus())
                .email(currentUser.getEmail())
                .cardNumber(currentUser.getCards().get(0).getNumber())
                .balance(currentUser.getCards().get(0).getBalance())
                .build();
    }

    @Override
    public UserData updateEmail(UUID userId, String email) {
        UserEntity currentUser = userRepository.findById(userId).orElseThrow(
                () -> ServiceException.builder()
                        .message("Пользователь с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build()
        );

        userValidation(currentUser);

        userRepository.updateUserEmail(userId, email);

        //Уверен, что будет, потому что впервый раз бросило бы ошибку
        UserEntity updatedUser = userRepository.findById(userId).get();

        IndividualEntity individual = individualRepository.findByPhoneNumber(updatedUser.getPhoneNumber());

        return UserData.builder()
                .firstName(individual.getFirstName())
                .lastName(individual.getLastName())
                .iin(individual.getIin())
                .status(individual.getMaritalStatus())
                .email(updatedUser.getEmail())
                .cardNumber(updatedUser.getCards().get(0).getNumber())
                .balance(updatedUser.getCards().get(0).getBalance())
                .build();
    }
}
