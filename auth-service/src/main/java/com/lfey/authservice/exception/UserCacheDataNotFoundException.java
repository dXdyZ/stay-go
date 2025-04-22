package com.lfey.authservice.exception;

public class UserCacheDataNotFoundException extends RuntimeException {
    public UserCacheDataNotFoundException(String message) {
        super(message);
    }
}
