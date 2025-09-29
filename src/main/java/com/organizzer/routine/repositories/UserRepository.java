package com.organizzer.routine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.organizzer.routine.models.UsersModel;

@Repository
public interface UserRepository extends JpaRepository<UsersModel, Integer> {
    
    @Query("SELECT COUNT(u) FROM UsersModel u")
    Long countUsers();
    
    @Query("SELECT u FROM UsersModel u WHERE u.userEmail = :email")
    UsersModel findByEmail(@Param("email") String email);
    
    @Query("SELECT u FROM UsersModel u WHERE u.userEmail = :email AND u.userName = :name")
    UsersModel findByEmailAndName(@Param("email") String email, @Param("name") String name);
    
    @Modifying
    @Query("UPDATE UsersModel u SET u.userImagePerfil = :fileName WHERE u.userId = :userId")
    void updateUserImage(@Param("fileName") String fileName, @Param("userId") Integer userId);
    
    @Modifying
    @Query("UPDATE UsersModel u SET u.userName = :userName, u.userEmail = :userEmail, u.userPassword = :userPassword WHERE u.userId = :userId")
    void updateUserCredentials(
        @Param("userName") String userName,
        @Param("userEmail") String userEmail,
        @Param("userPassword") String userPassword,
        @Param("userId") Integer userId
    );
    
    @Modifying
    @Query("UPDATE UsersModel u SET u.userName = :userName, u.userEmail = :userEmail WHERE u.userId = :userId")
    void updateUserCredentialsWithoutPassword(
        @Param("userName") String userName,
        @Param("userEmail") String userEmail,
        @Param("userId") Integer userId
    );
    
    @Query("SELECT u.userPassword FROM UsersModel u WHERE u.userId = :userId")
    String findUserPassword(@Param("userId") Integer userId);
    
    @Query("SELECT u FROM UsersModel u WHERE u.userId = :userId")
    UsersModel findUserById(@Param("userId") Integer userId);
} 