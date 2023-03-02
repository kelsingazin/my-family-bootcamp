package com.family.myfamily.controller;

import com.family.myfamily.repository.TestRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Тест")
@RestController
@RequestMapping("/api/test")
public class TestController {

    TestRepository testRepository;
    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/hello")
    @ApiOperation("Тестовый контроллер")
    public String hello() {
        return testRepository.getById(1L).getName();
    }


}
