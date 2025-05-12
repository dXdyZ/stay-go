package com.staygo.userservice.service;

import com.staygo.userservice.client.BookingClient;
import com.staygo.userservice.exception.HotelNotFoundException;
import feign.FeignException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class BookingClientService {
    private final BookingClient bookingClient;

    public BookingClientService(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @Cacheable(value = "hotelExistence", key = "#hotelId", unless = "#result == false ")
    public boolean validateHotelExists(Long hotelId) throws HotelNotFoundException{
        boolean exists = bookingClient.checkHotelExists(hotelId);
        if (!exists) {
            throw new HotelNotFoundException("Hotel with id %d not found".formatted(hotelId));
        }
        return exists;
    }
}
