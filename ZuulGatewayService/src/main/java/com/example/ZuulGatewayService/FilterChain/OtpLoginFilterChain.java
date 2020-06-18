package com.example.ZuulGatewayService.FilterChain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.ZuulGatewayService.AuthenticationProvider.OtpAuthenticationProvider;

@Configuration
@Order(2)
public class OtpLoginFilterChain extends WebSecurityConfigurerAdapter{

	@Autowired
	private OtpAuthenticationProvider otpAuthenticationProvider;
	
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//configuring authentication manager to use the OtpAuthenticationProvider for authentication
		auth.authenticationProvider(otpAuthenticationProvider);
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//using UsernamePasswordAuthenticationFilter however it is recommended to create our own custom AuthenticationFilter
		String filterProcessesUrl="/otplogin";
		UsernamePasswordAuthenticationFilter filter=new UsernamePasswordAuthenticationFilter();
		filter.setFilterProcessesUrl(filterProcessesUrl);
		filter.setUsernameParameter("user");
		filter.setPasswordParameter("otp");
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		filter.setAuthenticationFailureHandler(authenticationFailureHandler);
//		filter.setAuthenticationManager((authentication)->otpAuthenticationProvider.authenticate(authentication));
		
		
		http.requestMatcher(new AntPathRequestMatcher(filterProcessesUrl, HttpMethod.POST.name()))
			.addFilter(filter)
			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		
	}
	
}
