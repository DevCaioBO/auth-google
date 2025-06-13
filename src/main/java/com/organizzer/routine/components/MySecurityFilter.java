package com.organizzer.routine.components;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.organizzer.routine.repositories.UsersRepository;
import com.organizzer.routine.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MySecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsersRepository repo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			
		String path = request.getRequestURI();
		
		if(path.equals("/routine/register") || path.equals("/routine/login") || path.equals("/auth/google") || path.equals("/auth/google/callback")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		var token = this.recoverToken(request);
		
		if(token !=null) {
			var login = tokenService.validateToken(token);
			UserDetails user =  repo.findByUserName(login);
			
			var authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
	private String recoverToken(HttpServletRequest request) {
		
		var authHeader = request.getHeader("Authorization");
		
		if(authHeader == null)return  null;
		return authHeader.replace("Bearer ", ""); 
		
		
	}
	
	

}
