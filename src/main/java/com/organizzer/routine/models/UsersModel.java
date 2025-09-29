package com.organizzer.routine.models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.organizzer.routine.ENUMS.UsersRankEnum;
import com.organizzer.routine.ENUMS.UsersRolesEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name="users")
@Entity(name="UsersModel")

public class UsersModel implements UserDetails{
	
	
	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="user_id_google")
	private String userIdGoogle;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="user_password")
	private String userPassword;
	
	@Column(name="user_email")
	private String userEmail;
	
	@Column(name="user_role")
	@Enumerated(EnumType.STRING)
	private UsersRolesEnum userRoles;
	
	@Column(name="user_xp")
	private Integer userXp;
	
	@Column(name="user_lvl")
	private Integer userLvl;
	
	@Column(name="user_image_perfil")
	private String userImagePerfil;
	
	
	@Column(name="user_rank")
	@Enumerated(EnumType.STRING)
	private UsersRankEnum rank;
	
	
	
	@OneToMany(mappedBy="fkUserId",cascade=CascadeType.ALL)
	private List<TasksModel> tasks;
	
	@Column(name="user_money")
	private Integer money;
	
	
	
	
	


	
	







	
	
	




	







	public UsersModel() {
		super();
	}







	public UsersModel(Integer userId, String userIdGoogle, String userName, String userPassword, String userEmail,
			UsersRolesEnum userRoles, Integer userXp, Integer userLvl, String userImagePerfil, UsersRankEnum rank,
			List<TasksModel> tasks, Integer money) {
		super();
		this.userId = userId;
		this.userIdGoogle = userIdGoogle;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userEmail = userEmail;
		this.userRoles = userRoles;
		this.userXp = userXp;
		this.userLvl = userLvl;
		this.userImagePerfil = userImagePerfil;
		this.rank = rank;
		this.tasks = tasks;
		this.money = money;
	}




































	public UsersModel(Integer userId, String userIdGoogle, String userName, String userEmail, UsersRolesEnum userRoles,
			String userImagePerfil) {
		super();
		this.userId = userId;
		this.userIdGoogle = userIdGoogle;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userRoles = userRoles;
		this.userImagePerfil = userImagePerfil;
	
	}







	public UsersRankEnum getRank() {
		return rank;
	}







	public void setRank(UsersRankEnum rank) {
		this.rank = rank;
	}







	public Integer getMoney() {
		return money;
	}







	public void setMoney(Integer money) {
		this.money = money;
	}











	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.userRoles == userRoles.ADMIN) 
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
				new SimpleGrantedAuthority("ROLE_USER"));
		else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return userPassword;
	}


	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}




	public Integer getUserId() {
		return userId;
	}




	public void setUserId(Integer userId) {
		this.userId = userId;
	}




	public String getUserName() {
		return userName;
	}




	public String getUserIdGoogle() {
		return userIdGoogle;
	}







	public void setUserIdGoogle(String userIdGoogle) {
		this.userIdGoogle = userIdGoogle;
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




	public Integer getUserXp() {
		return userXp;
	}




	public void setUserXp(Integer userXp) {
		this.userXp = userXp;
	}




	public Integer getUserLvl() {
		return userLvl;
	}




	public void setUserLvl(Integer userLvl) {
		this.userLvl = userLvl;
	}




	public String getUserImagePerfil() {
		return userImagePerfil;
	}




	public void setUserImagePerfil(String userImagePerfil) {
		this.userImagePerfil = userImagePerfil;
	}




	public List<TasksModel> getTasks() {
		return tasks;
	}




	public void setTasks(List<TasksModel> tasks) {
		this.tasks = tasks;
	}




	
	
	

	

}
