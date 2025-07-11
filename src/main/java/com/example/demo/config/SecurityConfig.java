package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SecurityConfig {
	
	@Autowired
	private UnauthorizedEntryPoint unauthorizedEntryPoint;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.cors(Customizer.withDefaults()).csrf(
						csrf -> csrf.disable())
				.authorizeHttpRequests(
						authorize -> authorize
								.requestMatchers(request -> request.getRequestURI().startsWith("/users/authenticate")
										|| request.getRequestURI().startsWith("/users/forgotpwd")
										|| request.getRequestURI().startsWith("/users/changepwd")
										|| request.getRequestURI().startsWith("/users/createNewPassword")
										|| request.getRequestURI().startsWith("/users/forgotPassword")
										|| request.getRequestURI().startsWith("/users/otpValidate")
										|| request.getRequestURI().startsWith("/kbsharepoint/download")
										|| request.getRequestURI().startsWith("/localfile/download")
										|| request.getRequestURI().startsWith("/swagger-ui")
										|| request.getRequestURI().startsWith("/v3"))
								.permitAll().anyRequest().authenticated())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedEntryPoint))
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();

	}
	
	
	@Bean
	JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAuthenticationFilter();
	}

}
