package com.organizzer.routine.CONFIGS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.organizzer.routine.components.MySecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private MySecurityFilter mySecurityFilter;
	
	@Bean
	 public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.cors()
				.and()
				.sessionManagement(sessionManagement-> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> 
				authorize
				.requestMatchers(HttpMethod.POST,"/routine/register").permitAll()
				.requestMatchers(HttpMethod.POST,"/routine/login").permitAll()
				.requestMatchers(HttpMethod.GET,"/auth/google").permitAll()
				.requestMatchers(HttpMethod.GET,"/auth/google/callback").permitAll()
				
						.anyRequest().authenticated())
				.addFilterBefore(mySecurityFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
				
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
