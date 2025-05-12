package com.lfey.statygo.contoroller;

import com.lfey.statygo.component.PageResponse;
import com.lfey.statygo.dto.BookingDto;
import com.lfey.statygo.dto.BookingRoom;
import com.lfey.statygo.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/hotels/{hotelId}/period")
    public ResponseEntity<PageResponse<BookingDto>> getBookingByPeriod(
            @PathVariable Long hotelId,
            @RequestParam String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(bookingService.getHotelReservationOverPeriod(
               hotelId, startDate, endDate));
    }


    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<PageResponse<BookingDto>> getAllBookingByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.getAllHotelReservation(hotelId));
    }
}
