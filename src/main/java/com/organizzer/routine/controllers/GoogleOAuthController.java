package com.organizzer.routine.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
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
	private DataSource dataSource;
	
	@Autowired
	private UsersSpecifiedService userService;
	
	@Autowired
	private TokenService tokenService;
	
	//caio<- redirecionando o usuário para o google
	@GetMapping("/google")
	public void goToGoogle(HttpServletResponse response) throws IOException{
		String googleUriMapping = UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
				.queryParam("client_id","1076364224093-7a7q3ac2qtdqanr08o2qios025tds13q.apps.googleusercontent.com")
				.queryParam("redirect_uri","http://localhost:8080/auth/google/callback")
				.queryParam("response_type","code")
				.queryParam("scope","email profile")
				.build().toUriString();
		
		response.sendRedirect(googleUriMapping);
	}
	//caio<- verificando usuário fiel ou fidelizando usuário na minha base
	@GetMapping("google/callback")
	public void goToGoogleResponse(@RequestParam("code") String code,HttpServletResponse response) throws Exception{
		System.out.println("Código recebido do Google: " + code);
		GoogleTokenResponseDTO getToken = googleAuthService.extractTokenForCode(code);
		
		GoogleUserInfoDTO userGoogleinfo = googleAuthService.CollectGoogleUserInfoService(getToken.getAcessToken());
		
		Boolean isEmptyUser = userService.checkUserExists(userGoogleinfo.getEmail());
		
		
Boolean isAdmin = false;
		

		String sqlSearchIsAdmin = """
				SELECT COUNT(*) from users
				""";
		
		try (Connection con = dataSource.getConnection();
			PreparedStatement stmt = con.prepareStatement(sqlSearchIsAdmin);
			ResultSet rs = stmt.executeQuery();){
			
			if(rs.next() && rs.getInt(1) ==0) {
				isAdmin = true;
			}
			else {
				isAdmin=false;
			}
		}
		
		
		UsersModel OneUser = null;
		System.out.println(isEmptyUser);
		if(!isEmptyUser) {
			 OneUser = userService.CreateGoogleUserService(userGoogleinfo.getEmail(),userGoogleinfo.getName(),userGoogleinfo.getId(),userGoogleinfo.getPicture(),isAdmin?UsersRolesEnum.ADMIN.name():UsersRolesEnum.USER.name(),isAdmin?UsersRankEnum.RADIANTE.name():UsersRankEnum.BRONZE.name());
			 System.out.println("valor do = "+ OneUser + " tem user = "+ isEmptyUser);
		}else {
			OneUser = userService.ReadUserGoogleService(userGoogleinfo.getEmail(),userGoogleinfo.getName(),userGoogleinfo.getId(),userGoogleinfo.getPicture());
		}
		
		String jwtToken = tokenService.generateToken(OneUser);
		System.out.println(jwtToken);
	 response.sendRedirect("http://localhost:5173/home?token=" + jwtToken);
		
		
	}
	
}
