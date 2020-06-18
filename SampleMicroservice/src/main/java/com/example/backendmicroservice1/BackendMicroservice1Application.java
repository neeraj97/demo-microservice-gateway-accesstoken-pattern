package com.example.backendmicroservice1;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@RestController
public class BackendMicroservice1Application {

	public static void main(String[] args) {
		SpringApplication.run(BackendMicroservice1Application.class, args);
	}
	
	
	@GetMapping("/api/message")
	public Map<String,Object> someSecuredResource() {
		return Collections.singletonMap("message","Hi you have been succefully authenticated by zuul gateway and you are receiving this message received from the microservice ! sh...");
	}

}
