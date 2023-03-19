package com.family.myfamily.controller.rest;

import com.family.myfamily.repository.TestRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@SecurityRequirement(name = "myFamily")
public class TestController {

    TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/hello")
    @Operation(description = "Тестовый контроллер")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String hello() {
        return testRepository.getById(1L).getName();
    }
}
