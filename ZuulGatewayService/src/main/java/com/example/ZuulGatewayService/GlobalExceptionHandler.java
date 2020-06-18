package com.example.ZuulGatewayService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//handling generic exceptions ansd returning response with bad request status
	@ExceptionHandler
	public ResponseEntity<CustomExceptionResponse> handleException(Exception ex){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		CustomExceptionResponse response =new CustomExceptionResponse(status.value(),ex.getMessage(),System.currentTimeMillis());
		
		return new ResponseEntity<CustomExceptionResponse>(response, HttpStatus.BAD_REQUEST);
		
	}
}
