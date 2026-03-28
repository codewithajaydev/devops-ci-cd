package com.ajay.devops_cicd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DevopsCicdApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevopsCicdApplication.class, args);
	}

	@GetMapping("/")
	public String home() {
		return "DevOps CI/CD Pipeline Running 🚀";
	}

	@GetMapping("/health")
	public String health() {
		return "Application is Healthy ✅";
	}


}