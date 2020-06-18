package com.example.ZuulGatewayService.CustomerSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.ZuulGatewayService.Utilities.JwtTokenUtil;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		
		System.out.println(authentication.getClass());
		
		Map<String,Object> claims = new HashMap<String,Object>();
		claims.put("iss", "zuul-gateway");
		claims.put("aud","backend-microservices");
		
		if(authentication.getClass().equals(OAuth2AuthenticationToken.class)) {
			claims.put("isOAuth2Authenticated",true);
			claims.put("authorizedClientRegistrationId",((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId());
			session.setAttribute("JWT", jwtTokenUtil.generateToken(claims,authentication.getName()));
		}
		
		else if(authentication.getClass().equals(UsernamePasswordAuthenticationToken.class)) {
			claims.put("isOTPAuthenticated",true);
			session.setAttribute("JWT", jwtTokenUtil.generateToken(claims,authentication.getName()));
		}
		
		response.setStatus(202);
		
	}

}
