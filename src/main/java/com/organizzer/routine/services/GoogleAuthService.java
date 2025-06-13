package com.organizzer.routine.services;

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

@Service
public class GoogleAuthService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	public GoogleTokenResponseDTO extractTokenForCode(String code) {
		
		String SearchTokenUrl = "https://oauth2.googleapis.com/token";
		
		MultiValueMap<String, String> requiredBody = new LinkedMultiValueMap<>();
		requiredBody.add("code",code);
		requiredBody.add("client_id","1076364224093-7a7q3ac2qtdqanr08o2qios025tds13q.apps.googleusercontent.com");
		requiredBody.add("client_secret","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");
		requiredBody.add("redirect_uri","http://localhost:8080/auth/google/callback");
		requiredBody.add("grant_type","authorization_code");
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requiredBody,header);
		
		ResponseEntity<String> response = restTemplate.postForEntity(SearchTokenUrl, request, String.class);
		
		if(response.getStatusCode() == HttpStatus.OK) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(response.getBody());
				
				String acessToken = jsonNode.get("access_token").asText();
				String refreshToken = jsonNode.has("refresh_token")? jsonNode.get("refresh_token").asText(): null;
				Long expiresIn = jsonNode.get("expires_in").asLong();
				return new GoogleTokenResponseDTO(acessToken,refreshToken,expiresIn);
				
			}catch(Exception e) {
				throw new RuntimeException("erro ao converter o code em acess_token");
			}
		}else {
			throw new RuntimeException("erro ao obter o acess token já no if mesmo");
		}
	}
	
	public GoogleUserInfoDTO CollectGoogleUserInfoService(String acessToken) {
		String googleUserUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
		
		HttpHeaders header = new HttpHeaders();
		header.set("Authorization","Bearer "+acessToken);
		HttpEntity<String> entity = new HttpEntity<>(header);
		
		ResponseEntity<String> response = restTemplate.exchange(googleUserUrl, HttpMethod.GET,entity,String.class);
		
		if(response.getStatusCode() == HttpStatus.OK) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(response.getBody());
				String id = jsonNode.get("id").asText();
				String email = jsonNode.get("email").asText();
				String name = jsonNode.get("name").asText();
				String picture = jsonNode.get("picture").asText();
				return new GoogleUserInfoDTO(id,email,name,picture);
			}catch(Exception e) {
				throw new RuntimeException("erro ao tentar obter uma resposta "+ e);
			}
		}else {
			throw new RuntimeException("erro ao tentar pegar as informações do usuário");
		}
	}
}
