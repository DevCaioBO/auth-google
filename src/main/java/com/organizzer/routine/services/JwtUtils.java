package com.organizzer.routine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtils {

	@Value("${api.security.token.secret}")
	private String secret;
	
	public Integer getUserIdFromToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier jwtVerifier = JWT.require(algorithm).build();
			
			DecodedJWT decodedJWT = jwtVerifier.verify(token);
			
			return decodedJWT.getClaim("userId").asInt();
		}
		catch(JWTVerificationException exception) {
			throw new RuntimeException("token expirado ou invalido");
		}
	}
	
}
