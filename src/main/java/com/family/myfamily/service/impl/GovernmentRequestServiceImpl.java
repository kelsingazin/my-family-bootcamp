package com.family.myfamily.service.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.mapper.CityMapper;
import com.family.myfamily.model.dto.GovernmentRequestDto;
import com.family.myfamily.model.entities.CardEntity;
import com.family.myfamily.model.entities.CityEntity;
import com.family.myfamily.model.entities.GovernmentRequestEntity;
import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.model.enums.MaritalStatus;
import com.family.myfamily.model.enums.RequestStatus;
import com.family.myfamily.model.enums.RequestType;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.payload.request.ConfirmMarriage;
import com.family.myfamily.payload.request.RegisterCouple;
import com.family.myfamily.payload.response.Check;
import com.family.myfamily.payload.response.CitiesResponse;
import com.family.myfamily.payload.response.Notification;
import com.family.myfamily.repository.CardRepository;
import com.family.myfamily.repository.CityRepository;
import com.family.myfamily.repository.GovernmentRequestRepository;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.repository.UserRepository;
import com.family.myfamily.service.GovernmentRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GovernmentRequestServiceImpl implements GovernmentRequestService {

    private final UserRepository userRepository;
    private final GovernmentRequestRepository governmentRequestRepository;
    private final IndividualRepository individualRepository;
    private final ModelMapper modelMapper;
    private final CardRepository cardRepository;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    private void userValidation(UserEntity currentUser){
        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!contextUser.getPassword().equals(currentUser.getPassword())) {
            throw ServiceException
                    .builder()
                    .message("Клиент может иметь доступ только к своим запросам")
                    .errorCode(ErrorCode.AUTH_ERROR)
                    .build();
        }
    }

    private void payForMarriage(UserEntity user, String cardNumber) {
        CardEntity card = user.getCards().stream()
                .filter(cardEntity -> cardEntity.getNumber().equals(cardNumber))
                .toList()
                .get(0);

        if (card.getBalance() - 5000 < 0) {
            log.info("недостаточно средств для услуги");
            throw ServiceException.builder()
                    .message("недостаточно средств для услуги")
                    .errorCode(ErrorCode.NOT_ENOUGH_MONEY)
                    .build();
        }
        card.setBalance(card.getBalance() - 5000);
        cardRepository.save(card);
    }

    private void checkMarriage(IndividualEntity user, IndividualEntity partner) {
        if (user.getMaritalStatus().equals(MaritalStatus.MARRIED)
                || partner.getMaritalStatus().equals(MaritalStatus.MARRIED)) {
            throw ServiceException.builder()
                    .message("нельзя заключить брак, один из партнеров женат")
                    .errorCode(ErrorCode.ALREADY_MARRIED)
                    .build();
        }
        if (user.getGender().equals(partner.getGender())) {
            throw ServiceException.builder()
                    .message("нельзя заключить однополый брак")
                    .errorCode(ErrorCode.ALREADY_MARRIED)
                    .build();
        }
    }

    @Override
    @Transactional
    public Check registerCouple(RegisterCouple request) {

        IndividualEntity userIndividual = individualRepository.findByIin(request.getUserIin());
        IndividualEntity partnerIndividual = individualRepository.findByIin(request.getPartnerIin());

        UserEntity user = userRepository.findByPhoneNumber(userIndividual.getPhoneNumber());
        UserEntity partner = userRepository.findByPhoneNumber(partnerIndividual.getPhoneNumber());

        GovernmentRequestEntity requestEntity = governmentRequestRepository.findByRequestUserAndResponseUser(user, partner);
        if (requestEntity != null) {
            throw ServiceException.builder()
                    .errorCode(ErrorCode.ALREADY_REQUESTED)
                    .message("такая заявка уже существует")
                    .build();
        }

        checkMarriage(userIndividual, partnerIndividual);
        userValidation(user);

        GovernmentRequestEntity governmentRequest = GovernmentRequestEntity.builder()
                .date(new Date())
                .office(request.getOffice())
                .city(request.getCity())
                .isPartnerPaid(request.getIsUserPay())
                .requestUser(user)
                .responseUser(partner)
                .type(RequestType.MARRIAGE)
                .status(RequestStatus.WAITING)
                .build();

        if (request.getIsUserPay()) {
            payForMarriage(user, request.getCardNumber());
            GovernmentRequestEntity savedRequest = governmentRequestRepository.save(governmentRequest);
            return Check.builder()
                    .requestId(savedRequest.getId())
                    .sum(5000.0)
                    .date(new Date())
                    .build();
        } else {
            governmentRequestRepository.save(governmentRequest);
            return Check.builder()
                    .date(new Date())
                    .build();

        }
    }

    @Transactional
    @Override
    public Check confirmMarriage(ConfirmMarriage request) {

        GovernmentRequestEntity governmentRequest = governmentRequestRepository.findById(request.getGovernmentRequestId())
                .orElseThrow(() -> ServiceException.builder()
                        .message("нет запроса с таким идентификационным номером")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build());

        if (!governmentRequest.getStatus().equals(RequestStatus.WAITING)) {
            throw ServiceException.builder()
                    .message("заявка закрыта")
                    .errorCode(ErrorCode.NOT_ALLOWED)
                    .build();
        }

        UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> ServiceException.builder()
                        .message("пользователь с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build()
        );

        userValidation(user);

        if (request.getConfirm()) {

            if (governmentRequest.getIsPartnerPaid()) {

                governmentRequest.setStatus(RequestStatus.PROCESSED);
                governmentRequestRepository.save(governmentRequest);

            } else {
                payForMarriage(user, request.getCardNumber());
                governmentRequest.setStatus(RequestStatus.PROCESSED);
                governmentRequestRepository.save(governmentRequest);
                return Check.builder()
                        .requestId(governmentRequest.getId())
                        .sum(5000.0)
                        .date(new Date())
                        .build();
            }

        } else {
            governmentRequest.setStatus(RequestStatus.REJECTED);
            governmentRequestRepository.save(governmentRequest);
        }
        return Check.builder()
                .requestId(request.getGovernmentRequestId())
                .sum(0.0)
                .date(new Date())
                .build();
    }

    @Override
    public List<GovernmentRequestDto> getAllRequests(UUID id) {
        log.info("Получение всех запросов пользователя по userId {}", id);

        UserEntity currentUser = userRepository.findById(id).orElseThrow(
                () -> ServiceException.builder()
                        .message("нет запроса с таким идентификационным номером")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build()
        );

        userValidation(currentUser);

        List<GovernmentRequestEntity> list = governmentRequestRepository.findAllByRequestUser(currentUser);
        Type listType = new TypeToken<List<GovernmentRequestDto>>() {
        }.getType();

        return modelMapper.map(list, listType);
    }

    @Override
    public CitiesResponse getAllCities() {
        List<CityEntity> cityEntities = cityRepository.findAll();
        return CitiesResponse.builder()
                .sum(5000.0)
                .cityDtoLis(cityMapper.cityDtoList(cityEntities))
                .build();
    }

    @Override
    public List<Notification> getNotifications(UUID id) {
        log.info("Получение всех уведомлений пользователя по userId {}", id);

        UserEntity currentUser = userRepository.findById(id).orElseThrow(
                () -> ServiceException.builder()
                        .message("нет запроса с таким идентификационным номером")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build()
        );

        userValidation(currentUser);

        List<GovernmentRequestEntity> list = governmentRequestRepository.findAllByResponseUser(currentUser);
        list = list.stream().filter(
                governmentRequestEntity -> governmentRequestEntity.getStatus().equals(RequestStatus.WAITING)
        ).collect(Collectors.toList());
        Type listType = new TypeToken<List<Notification>>() {
        }.getType();

        return modelMapper.map(list, listType);
    }

}
