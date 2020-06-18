package com.example.ZuulGatewayService.FilterChain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)// this order is high enough , so this filter chain is used at last
public class DefaultFilterChain extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//match any request
		http.antMatcher("/**")
			.authorizeRequests().antMatchers("/unprotected","/ui/**","/getOTP/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))//configures to just send 401 unauthorized for unauthenticated requests 
			.and()
			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and()
			.logout().logoutSuccessHandler(logoutSuccessHandler).permitAll();
		
	}
	
}
