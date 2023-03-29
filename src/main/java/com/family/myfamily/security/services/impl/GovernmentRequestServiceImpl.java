package com.family.myfamily.security.services.impl;

import com.family.myfamily.payload.request.RegisterCouple;
import com.family.myfamily.payload.response.Check;
import com.family.myfamily.repository.GovernmentRequestRepository;
import com.family.myfamily.repository.IndividualRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class GovernmentRequestServiceImpl {

    private final GovernmentRequestRepository repository;
    private final IndividualRepository individualRepository;

    public Check registerCouple(RegisterCouple request){

        Double curBalance = 10000.0;

        return Check.builder()
                .sum(5000.0)
                .date(new Date())
                .build();
    }

}
