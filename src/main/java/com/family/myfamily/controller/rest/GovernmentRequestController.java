package com.family.myfamily.controller.rest;

import com.family.myfamily.model.dto.GovernmentRequestDto;
import com.family.myfamily.model.enums.RequestType;
import com.family.myfamily.payload.request.ConfirmMarriage;
import com.family.myfamily.payload.request.RegisterBabyRequest;
import com.family.myfamily.payload.request.RegisterCoupleRequest;
import com.family.myfamily.payload.response.BabyBirthCertificate;
import com.family.myfamily.payload.response.Check;
import com.family.myfamily.payload.response.CitiesResponse;
import com.family.myfamily.payload.response.MarriageCertificate;
import com.family.myfamily.payload.response.Notification;
import com.family.myfamily.service.GovernmentRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public Check registerCouple(@RequestBody RegisterCoupleRequest request) {
        log.info("POST request to register couple");
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

    @GetMapping("/cities")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CitiesResponse getAllCities(@RequestParam(name = "request_type") RequestType type)  {
        return governmentRequestService.getAllCities(type);
    }

    @GetMapping("/notifications/user-id/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public List<Notification> getNotifications(@PathVariable(name = "id") UUID id) {
        return governmentRequestService.getNotifications(id);
    }

    @GetMapping("/report/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String generateReport(@PathVariable UUID id) {
        return governmentRequestService.exportReport(id);
    }

    @PostMapping("/register-baby")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public Check registerBaby(@RequestBody RegisterBabyRequest request){
        return governmentRequestService.registerBaby(request);
    }

    @GetMapping("/marriage-certificate/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public MarriageCertificate getMarriageCertificate (@PathVariable(name = "id") UUID id) {
        return governmentRequestService.getMarriageCertificate(id);
    }

    @GetMapping("/baby-birth-certificate/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public BabyBirthCertificate getBabyBirthCertificate (@PathVariable(name = "id") UUID id) {
        return governmentRequestService.getBabyBirthCertificate(id);
    }
}
