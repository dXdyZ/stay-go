package com.lfey.statygo.contoroller;

import com.lfey.statygo.dto.BookingRoom;
import com.lfey.statygo.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public final static String USERNAME_HEADER = "X-User-Username";

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public void booingRoom(@Valid @RequestBody BookingRoom bookingRoom,
                           @RequestHeader(USERNAME_HEADER) String username) {
        bookingService.bookingRoom(bookingRoom, username);
    }
}
