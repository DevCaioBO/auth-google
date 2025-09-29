package com.organizzer.routine.repositories;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.organizzer.routine.ENUMS.UsersRankEnum;
import com.organizzer.routine.ENUMS.UsersRolesEnum;
import com.organizzer.routine.models.UsersModel;

@Repository
public class SpecifiedSearchUsersRepository {
	
	
	@Autowired
	private DataSource dataSource;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	private final String uploadDirectory ="uploads/";
	
	public List<String> ObtainUserLvl(Integer userId) throws Exception{
		
		Connection con = null;
		PreparedStatement stmt =null;
		ResultSet rs = null;
		String userRank = null;
		String userEmail = null;
		
		List<String> rankAndEmail = new ArrayList<String>();
		

		
		
		try {
			con = dataSource.getConnection();
			
			String sql = """
					SELECT user_rank,user_email FROM users WHERE user_id =?
					""";
			stmt = con.prepareStatement(sql);
			
			stmt.setInt(1, userId);
			
			rs = stmt.executeQuery();
			if(rs.next()) {
				
				userRank = UsersRankEnum.valueOf(rs.getString("user_rank")).name();
				userEmail = rs.getString("user_email");		
				rankAndEmail.add(userRank);
				rankAndEmail.add(userEmail);
			}
		}
		catch(SQLException exception) {
			exception.printStackTrace();
			throw new Exception("Algo deu errado na tentativa de obter o nivel de um usuário " + exception);
		}
		finally{
			if(con!=null) {
				con.close();
			}
			if(stmt!=null) {
				stmt.close();
			}
			if(rs!=null) {
				rs.close();
			}
		}
		return rankAndEmail;
	}
	
	// FAZENDO A PARTE DE UPLOAD DE IMAGEM DE ACORDO COM A EXISTÊNCIA DELA
	
	public String makeUploadImage(MultipartFile userImagePerfil,Integer userId) throws Exception {
		
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs =null;
		String currentFileName = null;
		String fileName = null;
		
		try {
			con = dataSource.getConnection();
			String sql = """
					SELECT user_image_perfil FROM users where user_id = ?
					""";
			
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, userId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				currentFileName = rs.getString("user_image_perfil");
			}
			
			if(currentFileName!=null && !currentFileName.isEmpty()) {
				File oldName = new File(uploadDirectory + currentFileName);
				if(oldName.exists()) {
					oldName.delete();
				}
			}
			
			String timestamp = String.valueOf(System.currentTimeMillis());
			fileName = timestamp + "_" + userImagePerfil.getOriginalFilename();
			String filePath = uploadDirectory + fileName;
			
			File dir = new File(uploadDirectory);
			
			if(!dir.exists()) {
				dir.mkdirs();
			}
			
			Files.write(Paths.get(filePath),userImagePerfil.getBytes());
	}
		finally {
			if(stmt!=null) {
				stmt.close();
			}
			if(con!=null) {
				con.close();
			}
			if(rs!=null) {
				rs.close();
			}
		}
	
		
		return fileName;
		}
	

	//FAZENDO A PARTE DE ALTERAR OU ADICIONAR UMA IMAGEM PARA O USUÁRIO

	public Boolean addOneImageForUser(String fileName,Integer userId) throws Exception {
		
		Connection con = null;
		PreparedStatement stmt = null;
		
		try {
			con = dataSource.getConnection();
			String sql = """
					UPDATE users SET user_image_perfil =? WHERE user_id = ?
					""";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, fileName);
			stmt.setInt(2, userId);
			
			stmt.executeUpdate();
		}
		catch(SQLException exception) {
			exception.printStackTrace();
			throw new Exception("algo deu errado tentando adicionar a imagem ao banco");
			
		}
		finally {
			if(con!=null) {
				con.close();
			}
			if(stmt!=null) {
				stmt.close();
			}
		}
		return true;
	}
	// FAZENDO MÉTODO PARA VERIFICAR SE A SENHA QUE O USUÁRIO ME MANDOU É IGUAL AO HASH GUARDADO
	
	public Boolean VerifyPasswordAndCompare(Integer userId,String currentUserPassword) throws Exception {
		
		
		
		
		
		
		String sql = """
				SELECT user_password FROM users WHERE user_id =? 
				""";
		String currentUserPasswordInBank = null;
		Boolean isValidNewPassword = false;
		try(Connection con = dataSource.getConnection(); 
				PreparedStatement stmt  = con.prepareStatement(sql); 
				){
		
			stmt.setInt(1, userId);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					currentUserPasswordInBank = rs.getString("user_password");
					isValidNewPassword =  passwordEncoder.matches(currentUserPassword,currentUserPasswordInBank);
					System.out.println("ta certo = "+ isValidNewPassword);
					return isValidNewPassword;
				}
			}

			
		}catch(SQLException exception) {
			exception.printStackTrace();
			throw new Exception("algo deu errado tentando comparar");
			
		}
		
		return false;
		
		
	}
	
	// FAZENDO A PARTE DE REALMENTE MUDAR ALGUNS DADOS VERMELHOS (TENDO EM VISTA O USUÁRIO PERMITIDO)
	
	public Boolean ChangeUserRedCredentials(Integer userId,String userName,String userEmail,String currentUserPassword,String newUserPassword) throws Exception{
		
		
		String sql = newUserPassword!=null && newUserPassword!=""?"""
				UPDATE users SET user_name = ?,user_email = ?,user_password = ? WHERE user_id = ?
				""":"""
				UPDATE users SET user_name = ?,user_email = ? WHERE user_id = ?
				""";
		String encryptedPassword =null;
		if(newUserPassword!=null && newUserPassword!="") {
			 encryptedPassword = new BCryptPasswordEncoder().encode(newUserPassword);
		}
		
		if(VerifyPasswordAndCompare(userId, currentUserPassword)) {
		try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(sql);){
			
			stmt.setString(1, userName);
			stmt.setString(2, userEmail);
			
			if( newUserPassword!=null && newUserPassword!="") {
				stmt.setString(3, encryptedPassword);
				stmt.setInt(4, userId);
			}else {
		
				stmt.setInt(3, userId);
			}
	
		
			
			stmt.executeUpdate();
				
		
			}
		catch(SQLException exception) {
			exception.printStackTrace();
			throw new Exception("É o usuário correto porém mesmo assim deu algo errado");
		}
		}
		else {
			System.out.println("ou algo deu errado, ou não é um usuário permitido que está mudando essas coisas");
			return false;
		}
		
		return true;
		
	}
	
	//caio<- Checando se um usuário google existe
	
	public Boolean checkUserExists(String email) throws Exception {
		
		String sqlRead = """
				SELECT user_email FROM users WHERE user_email =?
				""";
		
		Boolean userExists = false;
		
		try(Connection con = dataSource.getConnection()){
			try(PreparedStatement stmtRead = con.prepareStatement(sqlRead)){
				stmtRead.setString(1, email);
				try (ResultSet rs = stmtRead.executeQuery()){
					if(rs.next()) {
						userExists = true;
					}
					
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("erro ao tentar encontrar um usuário google");
			}catch(Exception e) {
				e.printStackTrace();
				throw new RuntimeException("erro muito estranho ao tentar encontrar um usuário google");
			}
			
		}
		return userExists;
	}
	//caio<- Registrando um usuário com o que o google forneceu
	public UsersModel CreateGoogleUser(String email,String name, String id,String picture,String role,String rank)throws Exception{
		
		String sqlCreate ="""
				INSERT INTO users(user_id_google,user_name,user_email,user_role,user_image_perfil,user_rank) VALUES(?,?,?,?,?,?)
				""";
		String sqlRead ="""
				SELECT user_id, user_id_google,user_name,user_email,user_role,user_image_perfil,user_rank FROM users WHERE user_email = ?
				""";
		UsersModel users = new UsersModel();
		
		try(Connection con = dataSource.getConnection()){
			try(PreparedStatement stmtCreate = con.prepareStatement(sqlCreate)){
				stmtCreate.setString(1, id);
				stmtCreate.setString(2, name);
				stmtCreate.setString(3, email);
				stmtCreate.setString(4, UsersRolesEnum.valueOf(role).name());
				stmtCreate.setString(5, picture);
				stmtCreate.setString(6, UsersRankEnum.valueOf(rank).name());
				stmtCreate.executeUpdate();
				
				try(PreparedStatement stmtRead = con.prepareStatement(sqlRead)){
					stmtRead.setString(1, email);
					try(ResultSet rs = stmtRead.executeQuery()){
						
						if(rs.next()) {
							
							users = new UsersModel(
									rs.getInt("user_id"),
									rs.getString("user_id_google"),
									rs.getString("user_name"),
									rs.getString("user_email"),
									UsersRolesEnum.valueOf(rs.getString("user_role")),
									rs.getString("user_image_perfil")
									
									);
							
						}
						return users;
					}
				}
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("erro ao tentar inserir um novo usuário google");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("erro ao tentar inserir um novo usuário google");
		}
		
	
	}
	public UsersModel ReadUserGoogle(String email,String name, String id,String picture){
		String sqlRead ="""
				SELECT user_id,user_id_google,user_name,user_email,user_role,user_image_perfil,user_rank FROM users WHERE user_email =? AND user_name = ? 
				""";
		UsersModel user =null;
		
		try(Connection con = dataSource.getConnection()){
			try(PreparedStatement stmtRead = con.prepareStatement(sqlRead)){
				stmtRead.setString(1, email);
				stmtRead.setString(2, name);

				try(ResultSet rs = stmtRead.executeQuery()){
					if(rs.next()) {
						user = new UsersModel(
								rs.getInt("user_id"),
								rs.getString("user_id_google"),
								rs.getString("user_name"),
								rs.getString("user_email"),
								UsersRolesEnum.valueOf(rs.getString("user_role")),
								rs.getString("user_image_perfil")
						
								);
					}
				}
		
			
				return user;
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("erro ao tentar inserir um novo usuário google");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("erro ao tentar inserir um novo usuário google");
		}
	}

	public Boolean isFirstUser() throws Exception {
		String sqlSearchIsAdmin = "SELECT COUNT(*) from users";
		
		try (Connection con = dataSource.getConnection();
			 PreparedStatement stmt = con.prepareStatement(sqlSearchIsAdmin);
			 ResultSet rs = stmt.executeQuery()) {
			
			if (rs.next() && rs.getInt(1) == 0) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw new Exception("Erro ao verificar se é o primeiro usuário", e);
		}
	}
}
