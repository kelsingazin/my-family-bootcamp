package com.family.myfamily.controller.rest;

import com.family.myfamily.model.dto.GovernmentRequestDto;
import com.family.myfamily.payload.request.ConfirmMarriage;
import com.family.myfamily.payload.request.RegisterCouple;
import com.family.myfamily.payload.response.Check;
import com.family.myfamily.payload.response.CitiesResponse;
import com.family.myfamily.service.GovernmentRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("api/government-requests")
@RequiredArgsConstructor
@Slf4j
public class GovernmentRequestController {

    private final GovernmentRequestService governmentRequestService;

    @PostMapping("/register-couple")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public Check registerCouple(@RequestBody RegisterCouple request) {
        return governmentRequestService.registerCouple(request);
    }

    @PutMapping("/register-couple")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public Check confirmMarriage(@RequestBody ConfirmMarriage request) {
        return governmentRequestService.confirmMarriage(request);
    }

    @GetMapping("/user-id/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public List<GovernmentRequestDto> getAllRequests(@PathVariable(name = "id") UUID id) {
        return governmentRequestService.getAllRequests(id);
    }

    @GetMapping("/register-couple")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CitiesResponse getAllCities(){
        return governmentRequestService.getAllCities();
    }

}
