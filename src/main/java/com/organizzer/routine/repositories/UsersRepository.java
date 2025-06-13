package com.organizzer.routine.repositories;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.organizzer.routine.models.UsersModel;

public interface UsersRepository extends JpaRepository<UsersModel, Integer> {
	UserDetails findByUserName(String userName);
	UserDetails findByUserEmail(String userEmail);
	

}
