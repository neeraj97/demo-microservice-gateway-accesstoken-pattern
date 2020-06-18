package com.example.ZuulGatewayService.Utilities;

import java.util.regex.Pattern;

public class ValidatorUtility {
	
	//precompiling the patterns for efficiency
	private static Pattern mobileNumberPattern = Pattern.compile("^\\+[1-9]\\d{1,14}$");
	
	
	private static Pattern emailIdPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
	//
	
	public static boolean isValidMobileNumber(String number) {
		return mobileNumberPattern.matcher(number).matches();
	}
	
	public static boolean isValidEmailId(String emailId) {
		return emailIdPattern.matcher(emailId).matches();
	}
}
