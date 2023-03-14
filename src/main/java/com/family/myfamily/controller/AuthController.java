package com.family.myfamily.controller;

import com.family.myfamily.payload.request.LoginRequest;
import com.family.myfamily.payload.response.JwtResponse;
import com.family.myfamily.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @GetMapping("/signin")
    public JwtResponse authenticateUser(@Valid @RequestParam(name = "phoneNumber") String phoneNumber,
                                        @RequestParam(name = "password") String password) {
        logger.info("GET request to authenticate");
        return authService.authenticateUser(new LoginRequest(phoneNumber, password));
    }

}
