package com.family.myfamily.security.services.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.model.dto.GovernmentRequestDto;
import com.family.myfamily.model.entities.GovernmentRequestEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.model.enums.RequestType;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.payload.request.ConfirmMarriage;
import com.family.myfamily.payload.request.RegisterCouple;
import com.family.myfamily.payload.response.Check;
import com.family.myfamily.repository.GovernmentRequestRepository;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.repository.UserRepository;
import com.family.myfamily.security.services.GovernmentRequestService;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class GovernmentRequestServiceImpl implements GovernmentRequestService {

    private final UserRepository userRepository;
    private final GovernmentRequestRepository governmentRequestRepository;
    private final IndividualRepository individualRepository;
    private final ModelMapper modelMapper;

    private void payForMarriage(UserEntity user){
        Double curBalance = user.getBalance();
        if (curBalance - 5000 < 0) {
            log.info("недостаточно средств для услуги");
            throw ServiceException.builder()
                    .message("недостаточно средств для услуги")
                    .errorCode(ErrorCode.NOT_ENOUGH_MONEY)
                    .build();
        }
        curBalance -= 5000;
        user.setBalance(curBalance);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public Check registerCouple(RegisterCouple request){

        UserEntity user = individualRepository.findByIin(request.getUserIin()).getUser();
        UserEntity partner = individualRepository.findByIin(request.getPartnerIin()).getUser();

        GovernmentRequestEntity governmentRequest = GovernmentRequestEntity.builder()
                .date(new Date())
                .office(request.getOffice())
                .city(request.getCity())
                .isPartnerPaid(request.getIsUserPay())
                .requestUser(user)
                .responseUser(partner)
                .type(RequestType.MARRIAGE)
                .status("waiting")
                .build();

        if (request.getIsUserPay()){
            payForMarriage(user);
            governmentRequestRepository.save(governmentRequest);
            return Check.builder()
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
    public Check confirmMarriage(ConfirmMarriage request){

        GovernmentRequestEntity governmentRequest = governmentRequestRepository.findById(request.getGovernmentRequestId())
                .orElseThrow(()->ServiceException.builder()
                        .message("нет запроса с таким идентификационным номером")
                        .errorCode(ErrorCode.RESOURCE_NOT_FOUND)
                        .build());

        UserEntity user = userRepository.findById(request.getUserId());

        if (request.getConfirm()){

            if (governmentRequest.getIsPartnerPaid()) {

                governmentRequest.setStatus("processed");
                governmentRequestRepository.save(governmentRequest);

            } else {
                payForMarriage(user);
                governmentRequest.setStatus("processed");
                governmentRequestRepository.save(governmentRequest);
                return Check.builder()
                        .sum(5000.0)
                        .date(new Date())
                        .build();
            }

        } else {
            governmentRequest.setStatus("rejected");
            governmentRequestRepository.save(governmentRequest);
        }
        return Check.builder()
                .date(new Date())
                .build();
    }

    @Override
    public List<GovernmentRequestDto> getAllRequests(UUID id){
        log.info("Получение всех запросов пользователя по userId {}", id);

        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity currentUser = userRepository.findById(id);

        if (contextUser.getPassword().equals(currentUser.getPassword())) {
            List<GovernmentRequestEntity> list = governmentRequestRepository.findAllByRequestUser(currentUser);
            Type listType = new TypeToken<List<GovernmentRequestDto>>() {
            }.getType();

            return modelMapper.map(list, listType);
        } else {
            throw ServiceException
                    .builder()
                    .message("Клиент может иметь доступ только к своим запросам")
                    .errorCode(ErrorCode.AUTH_ERROR)
                    .build();
        }

    }

}
