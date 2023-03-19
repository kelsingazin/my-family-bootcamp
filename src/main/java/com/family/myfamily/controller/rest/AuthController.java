package com.family.myfamily.controller.rest;

import com.family.myfamily.payload.request.LoginRequest;
import com.family.myfamily.payload.response.JwtResponse;
import com.family.myfamily.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signin")
    public JwtResponse authenticateUser(@RequestParam(name = "phoneNumber") String phoneNumber,
                                        @RequestParam(name = "password") String password) {
        log.info("GET request to authenticate");
        return authService.authenticateUser(new LoginRequest(phoneNumber, password));
    }
}
