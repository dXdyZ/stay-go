package com.staygo.userservice.exception;

public class DuplicateRoleException extends RuntimeException{
    public DuplicateRoleException() {
        super("User already has this role");
    }
}
