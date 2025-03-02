package com.lfey.authservice.exception;

public class UserRegNotFoundException extends RuntimeException {
    public UserRegNotFoundException(String message) {
        super(message);
    }
}
