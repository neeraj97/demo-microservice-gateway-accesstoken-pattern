package com.example.ZuulGatewayService.FilterChain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

//This will create a filter chain for OAuth authentication support and adds the filter to the filterchainproxy
@Configuration
@Order(3)
public class OAuth2FilterChain extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.requestMatchers().antMatchers("/login/oauth2/**","/oauth2/**")
				.and()
				// enabling csrf token to be sent in the cookie
				.csrf().disable()
//				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//				.and()
				.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
				// adding support for oauth authentication code grant
				.oauth2Login()
				.successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler);
	}

	// for more details refer to
	// https://spring.io/guides/tutorials/spring-boot-oauth2
	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {

		DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

		return request -> {
			OAuth2User user = delegate.loadUser(request);

			System.out.println(request.getClientRegistration().getRegistrationId() + " >>>>" + user);
			// this the place where mess with the user details
			// that is to insert the user if not present in database blah..blah...

			return user;
		};

	}

}
