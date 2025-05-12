package com.staygo.userservice.exception;

public class CustomNetworkException extends Exception {
    public CustomNetworkException() {
        super("Network error");
    }
}
