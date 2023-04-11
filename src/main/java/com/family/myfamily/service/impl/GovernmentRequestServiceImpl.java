package com.family.myfamily.service.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.mapper.CityMapper;
import com.family.myfamily.model.dto.GovernmentRequestDto;
import com.family.myfamily.model.entities.CardEntity;
import com.family.myfamily.model.entities.CityEntity;
import com.family.myfamily.model.entities.GovernmentRequestEntity;
import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.model.enums.Gender;
import com.family.myfamily.model.enums.MaritalStatus;
import com.family.myfamily.model.enums.RequestStatus;
import com.family.myfamily.model.enums.RequestType;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.payload.request.ConfirmMarriage;
import com.family.myfamily.payload.request.RegisterBabyRequest;
import com.family.myfamily.payload.request.RegisterCoupleRequest;
import com.family.myfamily.payload.response.BabyBirthCertificate;
import com.family.myfamily.payload.response.Check;
import com.family.myfamily.payload.response.CitiesResponse;
import com.family.myfamily.payload.response.MarriageCertificate;
import com.family.myfamily.payload.response.Notification;
import com.family.myfamily.repository.CardRepository;
import com.family.myfamily.repository.CityRepository;
import com.family.myfamily.repository.GovernmentRequestRepository;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.repository.UserRepository;
import com.family.myfamily.service.GovernmentRequestService;
import com.family.myfamily.utils.Constants;
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
    private final CardRepository cardRepository;
    private final CityRepository cityRepository;
    private final EmailSendService emailSendService;
    private final ModelMapper modelMapper;
    private final CityMapper cityMapper;

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

    private void makePayment(UserEntity user, UUID cardId, RequestType requestType) {
        CardEntity card = user.getCards().stream()
                .filter(cardEntity -> cardEntity.getId().equals(cardId))
                .toList()
                .get(0);
        Double payAmount = requestType.equals(RequestType.BABY_BIRTH) ? Constants.BABY_PAYMENT : Constants.MARRIAGE_PAYMENT;
        if (card.getBalance() - payAmount < 0) {
            log.info("недостаточно средств для услуги");
            throw ServiceException.builder()
                    .message("недостаточно средств для услуги")
                    .errorCode(ErrorCode.NOT_ENOUGH_MONEY)
                    .build();
        }
        card.setBalance(card.getBalance() - payAmount);
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
    public Check registerCouple(RegisterCoupleRequest request) {
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
            makePayment(user, request.getCardId(), RequestType.MARRIAGE);
            GovernmentRequestEntity savedRequest = governmentRequestRepository.save(governmentRequest);
            return Check.builder()
                    .requestId(savedRequest.getId())
                    .sum(Constants.MARRIAGE_PAYMENT)
                    .type(RequestType.MARRIAGE)
                    .date(new Date())
                    .build();
        } else {
            GovernmentRequestEntity savedRequest = governmentRequestRepository.save(governmentRequest);
            return Check.builder()
                    .requestId(savedRequest.getId())
                    .sum(0.0)
                    .type(RequestType.MARRIAGE)
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
                individualRepository.updateMarriageStatus(governmentRequest.getRequestUser().getPhoneNumber(), MaritalStatus.MARRIED);
                individualRepository.updateMarriageStatus(governmentRequest.getResponseUser().getPhoneNumber(), MaritalStatus.MARRIED);
                sendLetter(governmentRequest.getRequestUser(), governmentRequest.getResponseUser(), RequestStatus.PROCESSED);
            } else {
                makePayment(user, request.getCardId(), RequestType.MARRIAGE);
                governmentRequest.setStatus(RequestStatus.PROCESSED);
                governmentRequestRepository.save(governmentRequest);
                individualRepository.updateMarriageStatus(governmentRequest.getRequestUser().getPhoneNumber(), MaritalStatus.MARRIED);
                individualRepository.updateMarriageStatus(governmentRequest.getResponseUser().getPhoneNumber(), MaritalStatus.MARRIED);
                sendLetter(governmentRequest.getRequestUser(), governmentRequest.getResponseUser(), RequestStatus.PROCESSED);
                return Check.builder()
                        .requestId(governmentRequest.getId())
                        .sum(Constants.MARRIAGE_PAYMENT)
                        .type(RequestType.MARRIAGE)
                        .date(new Date())
                        .build();
            }
        } else {
            governmentRequest.setStatus(RequestStatus.REJECTED);
            governmentRequestRepository.save(governmentRequest);
            sendLetter(governmentRequest.getRequestUser(), governmentRequest.getResponseUser(), RequestStatus.REJECTED);
        }

        return Check.builder()
                .requestId(request.getGovernmentRequestId())
                .sum(0.0)
                .type(RequestType.MARRIAGE)
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
                .sum(Constants.MARRIAGE_PAYMENT)
                .cityDtoLis(cityMapper.cityDtoList(cityEntities))
                .build();
    }

    @Transactional
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

    @Override
    public Check registerBaby(RegisterBabyRequest request) {

        GovernmentRequestEntity requestEntity = governmentRequestRepository.findByBirthDate(request.getBirthDate());

        if (requestEntity != null) {
            throw ServiceException.builder()
                    .errorCode(ErrorCode.ALREADY_EXISTS)
                    .message("такая услуга уже существует")
                    .build();
        }

        log.info("регистрация рождения ребенка {}", request.getUserId());
        UserEntity requestingUser = userRepository.findById(request.getUserId()).orElseThrow(
                () -> ServiceException.builder()
                        .message("нет запроса с таким идентификационным номером")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build()
        );
        log.info("валдиация пользователя");
        userValidation(requestingUser);

        IndividualEntity mother = individualRepository.findByIin(request.getMotherIin());
        IndividualEntity father = individualRepository.findByIin(request.getFatherIin());

        if (!mother.getGender().equals(Gender.FEMALE) || !father.getGender().equals(Gender.MALE)) {
            throw ServiceException.builder()
                    .errorCode(ErrorCode.NOT_ALLOWED)
                    .message("неправильно ввели данные отца или мамы")
                    .build();
        }

        if (!requestingUser.getPhoneNumber().equals(mother.getPhoneNumber()) && !requestingUser.getPhoneNumber().equals(father.getPhoneNumber())) {
            throw ServiceException.builder()
                    .errorCode(ErrorCode.NOT_ALLOWED)
                    .message("вы можете зарегистрировать только своего ребенка")
                    .build();
        }

        log.info("произведение оплаты");
        makePayment(requestingUser, request.getCardId(), RequestType.BABY_BIRTH);

        GovernmentRequestEntity governmentRequest = GovernmentRequestEntity.builder()
                .requestUser(requestingUser)
                .country(request.getCountry())
                .city(request.getCity())
                .office(request.getOffice())
                .birthDate(request.getBirthDate())
                .father(father)
                .mother(mother)
                .status(RequestStatus.PROCESSED)
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .type(RequestType.BABY_BIRTH)
                .gender(request.getGender())
                .build();

        log.info("сохранение запроса");
        GovernmentRequestEntity savedRequest = governmentRequestRepository.save(governmentRequest);
        return Check.builder()
                .requestId(savedRequest.getId())
                .date(new Date())
                .type(RequestType.BABY_BIRTH)
                .sum(Constants.BABY_PAYMENT)
                .build();
    }

    @Override
    public MarriageCertificate getMarriageCertificate(UUID id) {
        UserEntity requestingUser = userRepository.findById(id).orElseThrow(
                () -> ServiceException.builder()
                        .message("нет запроса с таким идентификационным номером")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build()
        );
        log.info("валдиация пользователя");
        userValidation(requestingUser);

        log.info("поиск заявки о заключении брака");

        GovernmentRequestEntity requestEntity = governmentRequestRepository.findByRequestUserAndType(requestingUser, RequestType.MARRIAGE);
        if (requestEntity == null) {
            requestEntity = governmentRequestRepository.findByResponseUserAndType(requestingUser, RequestType.MARRIAGE);
        }

        IndividualEntity partnerOne = individualRepository.findByPhoneNumber(requestingUser.getPhoneNumber());
        IndividualEntity partnerTwo = individualRepository.findByPhoneNumber(requestEntity.getResponseUser().getPhoneNumber());

        IndividualEntity male = partnerOne.getGender().equals(Gender.MALE) ? partnerOne : partnerTwo;
        IndividualEntity female = partnerOne.getGender().equals(Gender.FEMALE) ? partnerOne : partnerTwo;

        return MarriageCertificate.builder()
                .id(requestEntity.getId())
                .city(requestEntity.getCity())
                .office(requestEntity.getOffice())
                .maleIin(male.getIin())
                .maleFirstName(male.getFirstName())
                .maleLastName(male.getLastName())
                .maleMiddleName(male.getMiddleName())
                .maleNationality(male.getNationality())
                .femaleIin(female.getIin())
                .femaleFirstName(female.getFirstName())
                .femaleLastName(female.getLastName())
                .femaleMiddleName(female.getMiddleName())
                .femaleNationality(female.getNationality())
                .build();
    }

    @Override
    public BabyBirthCertificate getBabyBirthCertificate(UUID id) {
        UserEntity requestingUser = userRepository.findById(id).orElseThrow(
                () -> ServiceException.builder()
                        .message("нет запроса с таким идентификационным номером")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build()
        );
        log.info("валдиация пользователя");
        userValidation(requestingUser);

        log.info("поиск заявки о рождении ребенка");

        IndividualEntity userIndividual = individualRepository.findByPhoneNumber(requestingUser.getPhoneNumber());

        GovernmentRequestEntity governmentRequest = userIndividual.getGender().equals(Gender.MALE) ?
                governmentRequestRepository.findByFatherAndType(userIndividual, RequestType.BABY_BIRTH) : governmentRequestRepository.findByMotherAndType(userIndividual, RequestType.BABY_BIRTH);

        return BabyBirthCertificate.builder()
                .id(governmentRequest.getId())
                .city(governmentRequest.getCity())
                .office(governmentRequest.getOffice())
                .country(governmentRequest.getCountry())
                .maleFirstName(governmentRequest.getFather().getFirstName())
                .maleLastName(governmentRequest.getFather().getLastName())
                .maleMiddleName(governmentRequest.getFather().getMiddleName())
                .maleNationality(governmentRequest.getFather().getNationality())
                .femaleFirstName(governmentRequest.getMother().getFirstName())
                .femaleLastName(governmentRequest.getMother().getLastName())
                .babyMiddleName(governmentRequest.getMother().getMiddleName())
                .femaleNationality(governmentRequest.getMother().getNationality())
                .babyGender(governmentRequest.getGender())
                .babyFirstName(governmentRequest.getFirstName())
                .babyLastName(governmentRequest.getLastName())
                .babyMiddleName(governmentRequest.getMiddleName())
                .build();
    }

    private void sendLetter(UserEntity recipient, UserEntity partner, RequestStatus requestStatus) {
        String title = Constants.MARRIAGE_CONFIRM;
        String text = RequestStatus.PROCESSED.equals(requestStatus) ?
                Constants.SUCCESS_MARRIAGE_CONFIRM : Constants.REJECTED_MARRIAGE_CONFIRM;

        emailSendService.sendEmail(recipient.getEmail(), title,
                String.format(text,
                        recipient.getFirstName(), recipient.getLastName(),
                        partner.getFirstName(), partner.getLastName()));

        emailSendService.sendEmail(partner.getEmail(), title,
                String.format(text,
                        partner.getFirstName(), partner.getLastName(),
                        recipient.getFirstName(), recipient.getLastName()));
    }
}
