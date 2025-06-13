package com.organizzer.routine.dtos;

public class UserPerfilDTO {
	
	private String userName;
	private String userEmail;
	private String userCurrentPassword;
	private String newUserPassword;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserCurrentPassword() {
		return userCurrentPassword;
	}
	public void setUserCurrentPassword(String userCurrentPassword) {
		this.userCurrentPassword = userCurrentPassword;
	}
	public String getNewUserPassword() {
		return newUserPassword;
	}
	public void setNewUserPassword(String newUserPassword) {
		this.newUserPassword = newUserPassword;
	}
	public UserPerfilDTO(String userName, String userEmail, String userCurrentPassword, String newUserPassword) {
		super();
		this.userName = userName;
		this.userEmail = userEmail;
		this.userCurrentPassword = userCurrentPassword;
		this.newUserPassword = newUserPassword;
	}
	public UserPerfilDTO() {
		super();
	}

	
	
	
}
