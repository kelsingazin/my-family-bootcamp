package com.family.myfamily.security.services.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.model.entities.GovernmentRequestEntity;
import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.model.enums.RequestType;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.payload.request.ConfirmMarriage;
import com.family.myfamily.payload.request.RegisterCouple;
import com.family.myfamily.payload.response.Check;
import com.family.myfamily.repository.GovernmentRequestRepository;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class GovernmentRequestServiceImpl {
    private final UserRepository userRepository;

    private final GovernmentRequestRepository repository;
    private final IndividualRepository individualRepository;

    public Check registerCouple(RegisterCouple request){

        UserEntity user = individualRepository.findByIin(request.getUserIin()).getUser();
        UserEntity partner = individualRepository.findByIin(request.getPartnerIin()).getUser();

        if (request.getIsUserPay()){
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
            repository.save(GovernmentRequestEntity.builder()
                    .date(new Date())
                    .office(request.getOffice())
                    .city(request.getCity())
                    .isPartnerPaid(true)
                    .requestUser(user)
                    .responseUser(partner)
                    .type(RequestType.MARRIAGE)
                    .status("waiting")
                    .build());
            return Check.builder()
                    .sum(5000.0)
                    .date(new Date())
                    .build();
        } else {
            repository.save(GovernmentRequestEntity.builder()
                    .date(new Date())
                    .office(request.getOffice())
                    .city(request.getCity())
                    .isPartnerPaid(false)
                    .requestUser(user)
                    .responseUser(partner)
                    .type(RequestType.MARRIAGE)
                    .status("waiting")
                    .build());
            return Check.builder()
                    .date(new Date())
                    .build();
        }
    }

    public Check confirmMarriage(ConfirmMarriage request){

        GovernmentRequestEntity governmentRequest = repository.findById(request.getGovernmentRequestId())
                .orElseThrow(()->ServiceException.builder()
                        .message("нет запроса с таким идентификационным номером")
                        .errorCode(ErrorCode.RESOURCE_NOT_FOUND)
                        .build());
        UserEntity user = userRepository.findById(request.getUserId());

        if (request.getConfirm()){

            if (governmentRequest.getIsPartnerPaid()) {
                governmentRequest.setStatus("processed");
                repository.save(governmentRequest);

            } else {
                Double balance = user.getBalance();
                if (balance - 5000 < 0) {
                    log.info("недостаточно средств для услуги");
                    throw ServiceException.builder()
                            .message("недостаточно средств для услуги")
                            .errorCode(ErrorCode.NOT_ENOUGH_MONEY)
                            .build();
                }
                balance-=5000;
                user.setBalance(balance);
                userRepository.save(user);
                governmentRequest.setStatus("processed");
                repository.save(governmentRequest);
                return Check.builder()
                        .sum(5000.0)
                        .date(new Date())
                        .build();
            }

        } else {
            governmentRequest.setStatus("rejected");
            repository.save(governmentRequest);
        }
        return Check.builder()
                .date(new Date())
                .build();
    }

}
