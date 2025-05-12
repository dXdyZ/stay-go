package com.staygo.userservice.exception;

public class DuplicateRoleException extends RuntimeException{
    public DuplicateRoleException(String message) {
        super(message);
    }
}
