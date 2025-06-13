package com.organizzer.routine.dtos;

import org.springframework.web.multipart.MultipartFile;

public class UsersImageDTO {
	
	
	private MultipartFile userImagePerfil;

	public MultipartFile getUserImagePerfil() {
		return userImagePerfil;
	}

	public void setUserImagePerfil(MultipartFile userImagePerfil) {
		this.userImagePerfil = userImagePerfil;
	}

	public UsersImageDTO() {
		super();
	}

	public UsersImageDTO(MultipartFile userImagePerfil) {
		super();
		this.userImagePerfil = userImagePerfil;
	}
	
	
}
