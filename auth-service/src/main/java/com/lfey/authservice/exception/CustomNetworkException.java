package com.lfey.authservice.exception;

public class CustomNetworkException extends Exception{
    public CustomNetworkException() {
        super("Network error");
    }
}
