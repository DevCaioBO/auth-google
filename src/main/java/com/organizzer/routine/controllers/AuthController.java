package com.organizzer.routine.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.organizzer.routine.ENUMS.UsersRankEnum;
import com.organizzer.routine.ENUMS.UsersRolesEnum;
import com.organizzer.routine.dtos.AuthenticationDTO;
import com.organizzer.routine.dtos.LoginResponseDTO;
import com.organizzer.routine.dtos.UserRegisterDTO;
import com.organizzer.routine.models.UsersModel;
import com.organizzer.routine.repositories.UsersRepository;
import com.organizzer.routine.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/routine")
public class AuthController {
	
	@Autowired
	private UsersRepository repo;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private DataSource dataSource;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO data) throws Exception{
		if(this.repo.findByUserName(data.getUserName())!=null || this.repo.findByUserEmail(data.getUserEmail())!=null) return ResponseEntity.status(HttpStatus.CONFLICT).body("usuário ja está cadastrado tente outros dados :(");

		
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.getUserPassword());
		

		
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
			
			
			String sql = """
					INSERT INTO users(user_name,user_email,user_password,user_role,user_xp,user_rank) VALUES(?,?,?,?,?,?)
					""";
			try(PreparedStatement stmtInsert = con.prepareStatement(sql);){
			
			
				stmtInsert.setString(1, data.getUserName());
				stmtInsert.setString(2, data.getUserEmail());
				stmtInsert.setString(3, encryptedPassword);
				stmtInsert.setString(4,isAdmin? UsersRolesEnum.ADMIN.name():UsersRolesEnum.USER.name());
			int initialXP = isAdmin? 999999:7650;
			stmtInsert.setInt(5, initialXP);
			stmtInsert.setString(6, UsersRankEnum.getRankForXp(initialXP).name());
			stmtInsert.executeUpdate();
			}
		}
		catch(SQLException exception) {
			throw new Exception("Algo deu errado na tentativa de inserir um novo usuário" + exception);
		}

		return ResponseEntity.ok("Novo usuário inserido com sucesso!");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.userName(), data.userPassword());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((UsersModel) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
		
		
	}
	

	
	
}
