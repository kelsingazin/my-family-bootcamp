package com.family.myfamily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MyfamilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyfamilyApplication.class, args);
	}

	TestRepository testRepository;
	public MyfamilyApplication(TestRepository testRepository) {
		this.testRepository = testRepository;
	}

	@GetMapping("/hello")
	public String hello() {
		return testRepository.getById(1L).getName();
	}

}
