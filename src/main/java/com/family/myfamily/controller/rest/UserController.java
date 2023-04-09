package com.family.myfamily.controller.rest;

import com.family.myfamily.payload.response.UserData;
import com.family.myfamily.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/user-id/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public UserData getUserData(@PathVariable(name = "id") UUID uuid) {
        log.info("GET запрос на данные пользователя по id = {}", uuid);
        return userService.getUserData(uuid);
    }

    @PatchMapping("/user-id/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserData updateEmail(@PathVariable UUID userId,
                                @RequestParam String email) {
        log.info("Обновление почты у пользователя по id = {}", userId);
        return userService.updateEmail(userId, email);
    }
}
