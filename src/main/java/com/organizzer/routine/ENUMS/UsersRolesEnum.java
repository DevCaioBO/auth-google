package com.organizzer.routine.ENUMS;

public enum UsersRolesEnum {
	ADMIN("admin"),
	USER("user");
	
	private String user_role;

	UsersRolesEnum(String user_role) {
		this.user_role = user_role;
	}
	
	public String getUserRole() {
		return user_role;
	}

}
