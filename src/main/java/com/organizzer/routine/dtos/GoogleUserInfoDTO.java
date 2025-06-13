package com.organizzer.routine.dtos;

public class GoogleUserInfoDTO {
	private String id;
	private String email;
	private String name;
	private String picture;
	public GoogleUserInfoDTO(String id, String email, String name, String picture) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.picture = picture;
	}
	public GoogleUserInfoDTO() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	
	
}
