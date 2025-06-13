package com.organizzer.routine.dtos;

import com.organizzer.routine.ENUMS.UsersRolesEnum;

public class UserRegisterDTO {
	
	private Integer userId;
	private String userName;
	private String userPassword;
	private String userEmail;
	private UsersRolesEnum userRoles;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public UsersRolesEnum getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(UsersRolesEnum userRoles) {
		this.userRoles = userRoles;
	}
	
	
	

	


}
