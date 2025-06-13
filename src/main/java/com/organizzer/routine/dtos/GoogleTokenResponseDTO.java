package com.organizzer.routine.dtos;

public class GoogleTokenResponseDTO {
	private String acessToken;
	private String refreshToken;
	private Long expirationTime;
	public String getAcessToken() {
		return acessToken;
	}
	public void setAcessToken(String acessToken) {
		this.acessToken = acessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public Long getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}
	public GoogleTokenResponseDTO(String acessToken, String refreshToken) {
		super();
		this.acessToken = acessToken;
		this.refreshToken = refreshToken;

	}
	public GoogleTokenResponseDTO(String acessToken, String refreshToken, Long expirationTime) {
		super();
		this.acessToken = acessToken;
		this.refreshToken = refreshToken;
		this.expirationTime = expirationTime;
	}
	public GoogleTokenResponseDTO() {
		super();
	}
	
	
	
}
