package com.lfey.statygo.component.factory;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.PriceCalculate;
import com.lfey.statygo.dto.BookingDto;
import com.lfey.statygo.dto.BookingRoom;
import com.lfey.statygo.entity.Booking;
import com.lfey.statygo.entity.Room;

import java.time.Instant;

public class BookingFactory {
    public static Booking createBooking(Room freeRoom, String username, BookingRoom bookingRoom) {
        return Booking.builder()
                .room(freeRoom)
                .hotel(freeRoom.getHotel())
                .createDate(Instant.now())
                .startDate(CustomDateFormatter.localDateFormatter(bookingRoom.getStartDate()))
                .endDate(CustomDateFormatter.localDateFormatter(bookingRoom.getEndDate()))
                .username(username)
                .totalPrice(PriceCalculate.calculationTotalPrice(freeRoom.getPricePerDay(),
                        CustomDateFormatter.localDateFormatter(bookingRoom.getStartDate()),
                        CustomDateFormatter.localDateFormatter(bookingRoom.getEndDate())))
                .build();
    }

    public static BookingDto createBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .hotelName(booking.getHotel().getName())
                .roomNumber(booking.getRoom().getNumber())
                .bookingStatus(booking.getBookingStatus().name())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .username(booking.getUsername())
                .totalPrice(booking.getTotalPrice())
                .build();
    }
}
