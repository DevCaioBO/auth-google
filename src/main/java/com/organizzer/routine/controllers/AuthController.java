package com.organizzer.routine.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.organizzer.routine.ENUMS.UsersRankEnum;
import com.organizzer.routine.ENUMS.UsersRolesEnum;
import com.organizzer.routine.dtos.AuthenticationDTO;
import com.organizzer.routine.dtos.LoginResponseDTO;
import com.organizzer.routine.dtos.UserRegisterDTO;
import com.organizzer.routine.exceptions.UserServiceException;
import com.organizzer.routine.models.UsersModel;
import com.organizzer.routine.repositories.UserRepository;
import com.organizzer.routine.services.TokenService;
import com.organizzer.routine.services.UsersSpecifiedService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("routine")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UsersSpecifiedService userService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserRegisterDTO data) {
		
	    Map<String, String> response = new HashMap<>();
	
		try {
			// Verificar se o usuário já existe
			if (userService.checkUserExists(data.userEmail())) {
			    response.put("message", "Falha ao registrar user!");
			    return ResponseEntity.ok(response);

			}
			
			// Criar novo usuário
			UsersModel user = new UsersModel();
			user.setUserName(data.userName());
			user.setUserEmail(data.userEmail());
			user.setUserPassword(passwordEncoder.encode(data.userPassword()));
			user.setUserRoles(UsersRolesEnum.USER);
			user.setRank(UsersRankEnum.BRONZE);
			
			userRepository.save(user);
			
			// Salvar usuário
		/*	userService.createGoogleUserService(
				user.getUserEmail(),
				user.getUserName(),
				null, // Não é usuário Google
				null, // Sem imagem de perfil inicial
				user.getUserRoles().name(),
				user.getRank().name()
			);*/
		    response.put("message", "Usuário registrado com sucesso!");
		    return ResponseEntity.ok(response);
		
			
		} catch (Exception e) {
			throw new UserServiceException("Erro ao registrar usuário", e);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AuthenticationDTO data) {
		try {
			var usernamePassword = new UsernamePasswordAuthenticationToken(data.userEmail(), data.userPassword());
			var auth = this.authenticationManager.authenticate(usernamePassword);
			
			var token = tokenService.generateToken((UsersModel) auth.getPrincipal());
			
			return ResponseEntity.ok(new LoginResponseDTO(token));
			
		} catch (Exception e) {
			throw new UserServiceException("Erro ao realizar login", e);
		}
	}
}
