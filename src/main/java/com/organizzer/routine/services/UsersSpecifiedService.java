package com.organizzer.routine.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.organizzer.routine.ENUMS.UsersRankEnum;
import com.organizzer.routine.ENUMS.UsersRolesEnum;
import com.organizzer.routine.exceptions.UserServiceException;
import com.organizzer.routine.models.UsersModel;
import com.organizzer.routine.repositories.UserRepository;

@Service
public class UsersSpecifiedService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<String> obtainUserLvl(Integer userId) {
		try {
			UsersModel user = userRepository.findUserById(userId);
			if (user == null) {
				throw new UserServiceException("Usuário não encontrado");
			}
			
			if (user.getRank() == null || user.getUserEmail() == null) {
				throw new UserServiceException("Dados do usuário incompletos");
			}
			
			return List.of(
				user.getRank().name(),
				user.getUserEmail()
			);
		} catch (Exception e) {
			throw new UserServiceException("Erro ao obter nível do usuário", e);
		}
	}
	
	@Transactional
	public String makeUploadImageService(MultipartFile userImagePerfil, Integer userId) {
		try {
			String timestamp = String.valueOf(System.currentTimeMillis());
			String fileName = timestamp + "_" + userImagePerfil.getOriginalFilename();
			userRepository.updateUserImage(fileName, userId);
			return fileName;
		} catch (Exception e) {
			throw new UserServiceException("Erro ao fazer upload da imagem", e);
		}
	}
	
	@Transactional
	public Boolean addOneImageForUser(String fileName, Integer userId) {
		try {
			userRepository.updateUserImage(fileName, userId);
			return true;
		} catch (Exception e) {
			throw new UserServiceException("Erro ao adicionar imagem ao usuário", e);
		}
	}
	
	public Boolean verifyPasswordAndCompareService(Integer userId, String currentUserPassword) {
		try {
			String storedPassword = userRepository.findUserPassword(userId);
			return passwordEncoder.matches(currentUserPassword, storedPassword);
		} catch (Exception e) {
			throw new UserServiceException("Erro ao verificar senha", e);
		}
	}
	
	@Transactional
	public Boolean changeUserRedCredentialsService(Integer userId, String userName, String userEmail, 
			String currentUserPassword, String userPassword) {
		try {
			if (verifyPasswordAndCompareService(userId, currentUserPassword)) {
				if (userPassword != null && !userPassword.isEmpty()) {
					String encryptedPassword = passwordEncoder.encode(userPassword);
					userRepository.updateUserCredentials(userName, userEmail, encryptedPassword, userId);
				} else {
					userRepository.updateUserCredentialsWithoutPassword(userName, userEmail, userId);
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new UserServiceException("Erro ao atualizar credenciais do usuário", e);
		}
	}
	
	public Boolean checkUserExists(String email) {
		try {
			return userRepository.findByEmail(email) != null;
		} catch (Exception e) {
			throw new UserServiceException("Erro ao verificar existência do usuário", e);
		}
	}
	
	@Transactional
	public UsersModel createGoogleUserService(String email, String name, String id, String picture, 
			String role, String rank) {
		try {
			UsersModel user = new UsersModel();
			user.setUserIdGoogle(id);
			user.setUserName(name);
			user.setUserEmail(email);
			user.setUserRoles(UsersRolesEnum.valueOf(role));
			user.setUserImagePerfil(picture);
			user.setRank(UsersRankEnum.valueOf(rank));
			return userRepository.save(user);
		} catch (Exception e) {
			throw new UserServiceException("Erro ao criar usuário Google", e);
		}
	}
	
	public UsersModel readUserGoogleService(String email, String name, String id, String picture) {
		try {
			return userRepository.findByEmailAndName(email, name);
		} catch (Exception e) {
			throw new UserServiceException("Erro ao ler usuário Google", e);
		}
	}
	
	public Boolean isFirstUser() {
		try {
			return userRepository.countUsers() == 0;
		} catch (Exception e) {
			throw new UserServiceException("Erro ao verificar se é o primeiro usuário", e);
		}
	}
}
