package com.organizzer.routine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.organizzer.routine.dtos.GoogleTokenResponseDTO;
import com.organizzer.routine.dtos.GoogleUserInfoDTO;
import com.organizzer.routine.exceptions.GoogleAuthException;

@Service
public class GoogleAuthService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;
	
	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String clientSecret;
	
	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String redirectUri;
	
	public GoogleTokenResponseDTO extractTokenForCode(String code) {
		String searchTokenUrl = "https://oauth2.googleapis.com/token";
		
		MultiValueMap<String, String> requiredBody = new LinkedMultiValueMap<>();
		requiredBody.add("code", code);
		requiredBody.add("client_id", clientId);
		requiredBody.add("client_secret", clientSecret);
		requiredBody.add("redirect_uri", redirectUri);
		requiredBody.add("grant_type", "authorization_code");
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requiredBody, header);
		
		ResponseEntity<String> response = restTemplate.postForEntity(searchTokenUrl, request, String.class);
		
		if (response.getStatusCode() == HttpStatus.OK) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(response.getBody());
				
				String accessToken = jsonNode.get("access_token").asText();
				String refreshToken = jsonNode.has("refresh_token") ? jsonNode.get("refresh_token").asText() : null;
				Long expiresIn = jsonNode.get("expires_in").asLong();
				return new GoogleTokenResponseDTO(accessToken, refreshToken, expiresIn);
				
			} catch (Exception e) {
				throw new GoogleAuthException("Erro ao converter o código em access_token", e);
			}
		} else {
			throw new GoogleAuthException("Erro ao obter o access token: " + response.getStatusCode());
		}
	}
	
	public GoogleUserInfoDTO collectGoogleUserInfo(String accessToken) {
		String googleUserUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
		
		HttpHeaders header = new HttpHeaders();
		header.set("Authorization", "Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<>(header);
		
		ResponseEntity<String> response = restTemplate.exchange(googleUserUrl, HttpMethod.GET, entity, String.class);
		
		if (response.getStatusCode() == HttpStatus.OK) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(response.getBody());
				String id = jsonNode.get("id").asText();
				String email = jsonNode.get("email").asText();
				String name = jsonNode.get("name").asText();
				String picture = jsonNode.get("picture").asText();
				return new GoogleUserInfoDTO(id, email, name, picture);
			} catch (Exception e) {
				throw new GoogleAuthException("Erro ao obter informações do usuário", e);
			}
		} else {
			throw new GoogleAuthException("Erro ao obter informações do usuário: " + response.getStatusCode());
		}
	}
}
