package com.example.ZuulGatewayService.Utilities;


//this interface defines interface for otp service providers for generating and verifying OTP
public interface OtpServiceProvider{
	
	public boolean VerifyOTP(String user, String token);
	
	public String GenerateOTP(String user);
	
}
