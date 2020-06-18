package com.example.ZuulGatewayService;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.ZuulGatewayService.Utilities.OtpServiceProvider;
import com.example.ZuulGatewayService.Utilities.ValidatorUtility;

@SpringBootApplication
@EnableZuulProxy
@RestController
public class ZuulGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulGatewayServiceApplication.class, args);
	}

	@GetMapping("/unprotected")
	public Map<String,Object> someUnprotectedResource() {
		return Collections.singletonMap("message", "Hey Iam not protected");
	}
	
//	@GetMapping("/user")
//	public Principal getUser(@AuthenticationPrincipal Principal principal) {
//		return principal;
//	}
//	
	@Autowired
	private OtpServiceProvider otpServiceProvider;
	
	@GetMapping("/getOTP/{user}")
	public void getOTP(@PathVariable String user) {
		user="+"+user;
		if(!ValidatorUtility.isValidMobileNumber(user))
			throw new RuntimeException("Not a valid Number");
		otpServiceProvider.GenerateOTP(user);
	}
	
	@GetMapping("/isAuthenticated")
	public boolean isAuthenticated(Authentication auth ) {
		return auth.isAuthenticated();
	}
	
}
