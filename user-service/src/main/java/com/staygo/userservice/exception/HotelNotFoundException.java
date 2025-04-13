package com.staygo.userservice.exception;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException() {
        super("Hotel not found");
    }
}
