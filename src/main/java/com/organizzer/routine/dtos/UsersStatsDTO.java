package com.organizzer.routine.dtos;

public class UsersStatsDTO {
	
	private Integer userLvl;

	public Integer getUserLvl() {
		return userLvl;
	}

	public void setUserLvl(Integer userLvl) {
		this.userLvl = userLvl;
	}

	public UsersStatsDTO(Integer userLvl) {
		super();
		this.userLvl = userLvl;
	}

	public UsersStatsDTO() {
		super();
	}
	
	
}
