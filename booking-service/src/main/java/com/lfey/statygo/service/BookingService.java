package com.lfey.statygo.service;

import com.lfey.statygo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    //TODO реализовать алгоритм и функцию бронирования

    public void bookingRoom() {

    }
}
