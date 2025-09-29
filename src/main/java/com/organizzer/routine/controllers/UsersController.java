package com.organizzer.routine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.organizzer.routine.dtos.UserPerfilDTO;
import com.organizzer.routine.dtos.UsersImageDTO;
import com.organizzer.routine.exceptions.UserServiceException;
import com.organizzer.routine.services.JwtUtils;
import com.organizzer.routine.services.UsersSpecifiedService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/organizzer")
public class UsersController {
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UsersSpecifiedService usersSpecifiedService;
	
	@GetMapping("lvl")
	public ResponseEntity<List<String>> collectUserLvl(@RequestHeader("Authorization") String token) {
		try {
			token = token.replace("Bearer ", "");
			Integer userId = jwtUtils.getUserIdFromToken(token);
			System.out.println("""
					///////////////////////////
					OLHA COMO QUE TÁ VINDO
					//////////////////////////
					 
					""" + userId);
			
			List<String> userInfo = usersSpecifiedService.obtainUserLvl(userId);
			return ResponseEntity.ok(userInfo);
		} catch (Exception e) {
			throw new UserServiceException("Erro ao obter nível do usuário", e);
		}
	}
	
	@PutMapping("upload")
	public ResponseEntity<String> makeUploadImage(
			@Valid @ModelAttribute UsersImageDTO data, 
			@RequestHeader("authorization") String token) {
		try {
			token = token.replace("Bearer ", "");
			Integer userId = jwtUtils.getUserIdFromToken(token);
			
			String fileName = usersSpecifiedService.makeUploadImageService(data.getUserImagePerfil(), userId);
			usersSpecifiedService.addOneImageForUser(fileName, userId);
			
			return ResponseEntity.ok("Imagem atualizada com sucesso");
		} catch (Exception e) {
			throw new UserServiceException("Erro ao fazer upload da imagem", e);
		}
	}
	
	@PutMapping("change/user")
	public ResponseEntity<Boolean> changeUserRedCredentials(
			@Valid @RequestBody UserPerfilDTO data, 
			@RequestHeader("Authorization") String token) {
		try {
			token = token.replace("Bearer ", "");
			Integer userId = jwtUtils.getUserIdFromToken(token);
			
			Boolean success = usersSpecifiedService.changeUserRedCredentialsService(
				userId,
				data.userName(),
				data.userEmail(),
				data.userCurrentPassword(),
				data.newUserPassword()
			);
			
			return ResponseEntity.ok(success);
		} catch (Exception e) {
			throw new UserServiceException("Erro ao atualizar credenciais do usuário", e);
		}
	}
}
