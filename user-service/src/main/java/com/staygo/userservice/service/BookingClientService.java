package com.staygo.userservice.service;

import com.staygo.userservice.client.BookingClient;
import com.staygo.userservice.exception.HotelNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class BookingClientService {
    private final BookingClient bookingClient;

    public BookingClientService(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @Cacheable(value = "hotelExists", key = "#hotelId")
    public boolean checkHotelExistsInBookingService(Long hotelId) throws HotelNotFoundException{
        if (bookingClient.checkHotelExists(hotelId)) {
            return true;
        } else throw new HotelNotFoundException();
    }
}
