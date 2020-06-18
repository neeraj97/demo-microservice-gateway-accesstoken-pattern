package com.example.ZuulGatewayService.AuthenticationProvider;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.ZuulGatewayService.Utilities.OtpServiceProvider;
import com.example.ZuulGatewayService.Utilities.ValidatorUtility;

//creating custom AuthenticationFilter for OTP Authenrication
@Component
public class OtpAuthenticationProvider implements AuthenticationProvider{

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	public OtpServiceProvider otpServiceProvider;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		logger.info("Inside OtpAuthenticationProvider");
		
		
		//get the user reference i.e., mobile no. / email Id..... and otp 
		String user = authentication.getName(); 
		String token = authentication.getCredentials().toString();
		
		logger.info(String.format("user : %s token : %s", user,token));
		
		//make sure that user string is a valid mobile num / email
		if(!ValidatorUtility.isValidMobileNumber(user) )//&& !ValidatorUtility.isValidEmailId(user))
			throw new BadCredentialsException("Invalid user details");
		
		boolean verificationResult = otpServiceProvider.VerifyOTP(user, token);
		
		logger.info(String.format("Verfication result: %s for user : %s token : %s", verificationResult,user,token));
		
		if(!verificationResult)
			throw new BadCredentialsException("Invalid OTP");
		
		//Sending null in place of credentials so as to prevent sensitive data exposure
		UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
		
		return userAuthentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}
