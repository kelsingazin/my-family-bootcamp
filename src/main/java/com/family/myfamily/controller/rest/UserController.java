package com.family.myfamily.controller.rest;

import com.family.myfamily.payload.response.UserData;
import com.family.myfamily.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public UserData getUserData(@PathVariable(name = "id") UUID uuid){
        log.info("GET запрос на данные пользователя по id = {}", uuid);
        return userService.getUserData(uuid);
    }

}
