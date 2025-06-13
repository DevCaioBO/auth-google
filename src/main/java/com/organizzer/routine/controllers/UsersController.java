package com.organizzer.routine.controllers;

import java.sql.SQLException;
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
	public List<String> CollectUserLvl(@RequestHeader("Authorization") String token) throws Exception {
		
			try {
			token = token.replace("Bearer ", "");
			Integer userId = jwtUtils.getUserIdFromToken(token);
			return usersSpecifiedService.ObtainUserLvl(userId);
			}
			catch(SQLException exception) {
				exception.printStackTrace();
				throw new Exception("Algo deu errado na tentativa de obter o lvl do usuário");
			}
	
	}
	
	@PutMapping("upload")
	public ResponseEntity makeUploadImage(@Valid @ModelAttribute UsersImageDTO data, @RequestHeader("authorization") String token) throws Exception {
		
		
		token = token.replace("Bearer ","");
		
		Integer userId = jwtUtils.getUserIdFromToken(token);
		
		String fileName = usersSpecifiedService.makeUploadImageService(data.getUserImagePerfil(), userId);
		
		usersSpecifiedService.addOneImageForUser(fileName, userId);
		
		return ResponseEntity.ok("Imagem atribuida corretamente eu acho por enquanto");
		
	}
	
	@PutMapping("change/user")
	public Boolean ChangeUserRedCredentials(@Valid 
			@RequestBody UserPerfilDTO data, 
			@RequestHeader("Authorization") String token) throws Exception{
		
	token = token.replace("Bearer ","");
	
	Integer userId = jwtUtils.getUserIdFromToken(token);
		
	try {
	usersSpecifiedService.ChangeUserRedCredentialsService(userId, 
			data.getUserName(), 
			data.getUserEmail(), 
			data.getUserCurrentPassword(),
			data.getNewUserPassword());
	}catch(SQLException exception) {
		exception.printStackTrace();
		throw new Exception("Deu algo errado logo já de cara");
	}
		
		return true;
		
	}
	
}
