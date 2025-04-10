package com.lfey.statygo.exception;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(Object message) {
        super("Hotel by: " + message + " not found");
    }
}
