package com.family.myfamily.controller.rest;

import com.family.myfamily.payload.response.Check;
import com.family.myfamily.security.services.impl.GovernmentRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/government-request")
@RequiredArgsConstructor
@Slf4j
public class GovernmentRequestController {

    private final GovernmentRequestService service;
    @PostMapping("/register-couple")
    public Check registerCouple(){
        return true;
    }
    @PutMapping ("/register-couple")
    public Boolean confirmMarriage(){
        return true;
    }

    @GetMapping("/{id}")
    public Boolean getAllRequests(){
        return true;
    }

}
