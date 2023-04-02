package com.family.myfamily.controller.rest;

import com.family.myfamily.model.dto.GovernmentRequestDto;
import com.family.myfamily.payload.request.ConfirmMarriage;
import com.family.myfamily.payload.request.RegisterCouple;
import com.family.myfamily.payload.response.Check;
import com.family.myfamily.service.GovernmentRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/government-request")
@RequiredArgsConstructor
@Slf4j
public class GovernmentRequestController {

    private final GovernmentRequestService service;
    @PostMapping("/register-couple")
    public Check registerCouple(@RequestBody RegisterCouple request){
        return service.registerCouple(request);
    }
    @PutMapping ("/register-couple")
    public Check confirmMarriage(@RequestBody ConfirmMarriage request){
        return service.confirmMarriage(request);
    }

    @GetMapping("/{id}")
    public List<GovernmentRequestDto> getAllRequests(@PathVariable(name = "id") UUID id){
        return service.getAllRequests(id);
    }

}
