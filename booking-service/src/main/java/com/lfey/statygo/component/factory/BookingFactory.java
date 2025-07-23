package com.lfey.statygo.component.factory;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.PriceCalculate;
import com.lfey.statygo.dto.BookingDto;
import com.lfey.statygo.dto.BookingRoomDto;
import com.lfey.statygo.entity.Booking;
import com.lfey.statygo.entity.Room;

import java.time.Instant;
import java.util.UUID;

public class BookingFactory {
    public static Booking createBooking(Room freeRoom, UUID publicId, BookingRoomDto bookingRoomDto) {
        return Booking.builder()
                .room(freeRoom)
                .hotel(freeRoom.getHotel())
                .createDate(Instant.now())
                .startDate(CustomDateFormatter.localDateFormatter(bookingRoomDto.getStartDate()))
                .endDate(CustomDateFormatter.localDateFormatter(bookingRoomDto.getEndDate()))
                .userPublicID(publicId)
                .totalPrice(PriceCalculate.calculationTotalPrice(freeRoom.getPricePerDay(),
                        CustomDateFormatter.localDateFormatter(bookingRoomDto.getStartDate()),
                        CustomDateFormatter.localDateFormatter(bookingRoomDto.getEndDate())))
                .guests(bookingRoomDto.getGuests())
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
                .userPublicId(booking.getUserPublicID())
                .totalPrice(booking.getTotalPrice())
                .guests(booking.getGuests())
                .build();
    }
}
