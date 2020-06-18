package com.example.backendmicroservice1.SecurityFilter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backendmicroservice1.Utitlity.JwtTokenUtil;

import io.jsonwebtoken.JwtException;

public class JwtAuthenticationOncePerRequestFilter extends OncePerRequestFilter{

	private JwtTokenUtil jwtTokenUtil;
	
	public JwtAuthenticationOncePerRequestFilter(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil=jwtTokenUtil;
	}

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String jwtToken = request.getHeader("JWT");
		
		String username=null;
		
		
		//Getting username from the jwt
		if(jwtToken!=null) {

			logger.info(String.format("JWT Token %s", jwtToken));
			
			try {
				if(!jwtTokenUtil.validateToken(jwtToken))
					throw new JwtException("Not Valid");
				
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				
			}
			catch(JwtException ex) {
				logger.info(String.format("Invalid Jwt %s",jwtToken));
			}
			
		}
		
		else {
			logger.warn("JWT token is not present");
		}
		
		//Get the SecurityContext for the current request processing thread
		SecurityContext securityctx = SecurityContextHolder.getContext();
		
		//if everthing is successful we populate that appropriate authentication in the current security context 
		//so that request is considered authenticated
		if(username!=null && securityctx.getAuthentication()==null) {
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,Collections.emptyList());
			
			securityctx.setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}

}
