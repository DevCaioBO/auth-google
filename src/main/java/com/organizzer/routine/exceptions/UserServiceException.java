package com.organizzer.routine.exceptions;

public class UserServiceException extends RuntimeException {
    
    public UserServiceException(String message) {
        super(message);
    }
    
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
} 