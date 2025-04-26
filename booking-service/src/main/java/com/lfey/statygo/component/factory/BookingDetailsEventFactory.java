package com.lfey.statygo.component.factory;

import com.lfey.statygo.dto.BookingDetailsEvent;
import com.lfey.statygo.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingDetailsEventFactory {

    public BookingDetailsEvent createBookingDetailsEvent(Booking booking, String username) {
        return BookingDetailsEvent.builder()
                .bookingId(booking.getId())
                .hotelName(booking.getHotel().getName())
                .hotelAddress(booking.getHotel().getAddress().getHumanReadableAddress())
                .startDate(booking.getStartDate().toString())
                .endDate(booking.getEndDate().toString())
                .roomType(booking.getRoom().getRoomType().name())
                .username(username)
                .totalPrice(booking.getTotalPrice())
                .bookingStatus(booking.getBookingStatus().name())
                .reservedRooms(1)
                .build();
    }

    public BookingDetailsEvent createBookingDetailsEventByGroup(BookingDetailsEvent first, int listSize) {
        return BookingDetailsEvent.builder()
                .hotelName(first.getHotelName())
                .hotelAddress(first.getHotelAddress())
                .roomType(first.getRoomType())
                .startDate(first.getStartDate())
                .endDate(first.getEndDate())
                .username(first.getUsername())
                .totalPrice(first.getTotalPrice() * listSize)
                .bookingStatus(first.getBookingStatus())
                .reservedRooms(listSize)
                .build();
    }
}
