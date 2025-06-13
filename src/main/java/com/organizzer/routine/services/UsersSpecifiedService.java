package com.organizzer.routine.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.organizzer.routine.ENUMS.UsersRolesEnum;
import com.organizzer.routine.models.UsersModel;
import com.organizzer.routine.repositories.SpecifiedSearchUsersRepository;

@Service
public class UsersSpecifiedService {
	
	@Autowired
	private SpecifiedSearchUsersRepository repo;
	
	public List<String> ObtainUserLvl(Integer userId) throws Exception {
		try {
			return repo.ObtainUserLvl(userId);
		}
		catch(SQLException exception) {
			exception.printStackTrace();
			throw new Exception("Algo deu errado ao tentar obter o lvl de um usuário");
		}
	}
	
	public String makeUploadImageService(MultipartFile userImagePerfil,Integer userId) throws Exception {
		return repo.makeUploadImage(userImagePerfil, userId);
	}
	
	public Boolean addOneImageForUser(String fileName,Integer userId) throws Exception {
		repo.addOneImageForUser(fileName, userId);
		return true;
	}
	
	public Boolean VerifyPasswordAndCompareService(Integer userId,String currentUserPassword) throws Exception {
		repo.VerifyPasswordAndCompare(userId, currentUserPassword);
		return true;
	}
	
	public Boolean ChangeUserRedCredentialsService(Integer userId,String userName,String userEmail,String currentUserPassword,String userPassword) throws Exception{
		repo.ChangeUserRedCredentials(userId, userName, userEmail, currentUserPassword, userPassword);
		return true;
	}
	
	public Boolean checkUserExists(String email) throws Exception {
		return repo.checkUserExists(email);
	}
	
	public UsersModel CreateGoogleUserService(String email,String name, String id,String picture,String role,String rank)throws Exception{
		return repo.CreateGoogleUser(email, name, id, picture,role,rank);
	}
	
	public UsersModel ReadUserGoogleService(String email,String name, String id,String picture){
		return repo.ReadUserGoogle(email, name, id, picture);
	}
}
