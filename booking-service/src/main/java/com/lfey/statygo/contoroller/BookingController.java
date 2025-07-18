package com.lfey.statygo.contoroller;

import com.lfey.statygo.component.PageResponse;
import com.lfey.statygo.contoroller.documentation.BookingControllerDocs;
import com.lfey.statygo.dto.BookingDto;
import com.lfey.statygo.dto.BookingHistoryDto;
import com.lfey.statygo.dto.BookingRoomDto;
import com.lfey.statygo.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController implements BookingControllerDocs {
    private final BookingService bookingService;

    public final static String USERNAME_HEADER = "X-User-Username";

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void booingRoom(@Valid @RequestBody BookingRoomDto bookingRoomDto,
                           @RequestHeader(USERNAME_HEADER) String username) {
        bookingService.bookingRoom(bookingRoomDto, username);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/history")
    public ResponseEntity<PageResponse<BookingHistoryDto>> getUserBookingHistory(
            @RequestHeader(USERNAME_HEADER) String username,
            @RequestParam(required = false, defaultValue = "0") int page) {
        return ResponseEntity.ok(bookingService.getUserBookingHistory(username, page));
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
