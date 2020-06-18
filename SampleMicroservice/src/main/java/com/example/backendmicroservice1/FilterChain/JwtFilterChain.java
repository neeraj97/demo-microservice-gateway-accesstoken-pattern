package com.example.backendmicroservice1.FilterChain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.backendmicroservice1.SecurityFilter.JwtAuthenticationOncePerRequestFilter;
import com.example.backendmicroservice1.Utitlity.JwtTokenUtil;

//filterchain config for JWT Authentication
@Configuration
@EnableWebSecurity
public class JwtFilterChain extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//we could have autowired this
		//but there is a catch, JwtAuthenticationOncePerRequestFilter is of type javax.servlet.Filter and
		//if a bean of javax.servlet.Filter is found , it is registered as filter along with spring security filter in the container
		//It would be registered outside the filterchainproxy
		JwtAuthenticationOncePerRequestFilter jwtFilter = new JwtAuthenticationOncePerRequestFilter(jwtTokenUtil);
		
		http.csrf().disable() //disabling csrf
			.anonymous().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //disabling session creation
			.and()
			.authorizeRequests().anyRequest().permitAll() //authentication required for requests
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
			.and()
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) //adding jwt token authentication filter
			.httpBasic().disable()
			.formLogin().disable(); 
	}
	
	
}
