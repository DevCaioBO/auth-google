package com.organizzer.routine.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.organizzer.routine.ENUMS.UsersRankEnum;
import com.organizzer.routine.ENUMS.UsersRolesEnum;
import com.organizzer.routine.dtos.GoogleTokenResponseDTO;
import com.organizzer.routine.dtos.GoogleUserInfoDTO;
import com.organizzer.routine.exceptions.GoogleAuthException;
import com.organizzer.routine.models.UsersModel;
import com.organizzer.routine.services.GoogleAuthService;
import com.organizzer.routine.services.TokenService;
import com.organizzer.routine.services.UsersSpecifiedService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("auth")
public class GoogleOAuthController {
	
	@Autowired
	private GoogleAuthService googleAuthService;
	
	@Autowired
	private UsersSpecifiedService userService;
	
	@Autowired
	private TokenService tokenService;
	
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;
	
	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String redirectUri;
	
	@Value("${frontend.url}")
	private String frontendUrl;
	
	@GetMapping("/google")
	public ResponseEntity<Void> goToGoogle(HttpServletResponse response) throws IOException {
		String googleUriMapping = UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
				.queryParam("client_id", clientId)
				.queryParam("redirect_uri", redirectUri)
				.queryParam("response_type", "code")
				.queryParam("scope", "email profile")
				.build().toUriString();
		
		response.sendRedirect(googleUriMapping);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("google/callback")
	public ResponseEntity<Void> goToGoogleResponse(@RequestParam("code") String code, HttpServletResponse response) {
		try {
			// Obter token de acesso do Google
			GoogleTokenResponseDTO tokenResponse = googleAuthService.extractTokenForCode(code);
			
			// Obter informações do usuário do Google
			GoogleUserInfoDTO userGoogleInfo = googleAuthService.collectGoogleUserInfo(tokenResponse.getAcessToken());
			
			// Verificar se o usuário já existe
			Boolean isEmptyUser = userService.checkUserExists(userGoogleInfo.getEmail());
			Boolean isAdmin = userService.isFirstUser();
			
			// Criar ou atualizar usuário
			UsersModel user;
			if (!isEmptyUser) {
				user = userService.createGoogleUserService(
					userGoogleInfo.getEmail(),
					userGoogleInfo.getName(),
					userGoogleInfo.getId(),
					userGoogleInfo.getPicture(),
					isAdmin ? UsersRolesEnum.ADMIN.name() : UsersRolesEnum.USER.name(),
					isAdmin ? UsersRankEnum.RADIANTE.name() : UsersRankEnum.BRONZE.name()
				);
			} else {
				user = userService.readUserGoogleService(
					userGoogleInfo.getEmail(),
					userGoogleInfo.getName(),
					userGoogleInfo.getId(),
					userGoogleInfo.getPicture()
				);
			}
			
			// Gerar token JWT
			String jwtToken = tokenService.generateToken(user);
			
			// Redirecionar para o frontend com o token
			response.sendRedirect(frontendUrl + "/home?token=" + jwtToken);
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			throw new GoogleAuthException("Erro durante o processo de autenticação com Google", e);
		}
	}
}
