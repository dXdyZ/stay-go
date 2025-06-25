package com.lfey.statygo.component.factory;

import com.lfey.statygo.dto.BookingHistoryDto;
import com.lfey.statygo.entity.Booking;

public class BookingHistoryFactory {
    private static String mainUrl;

    public static void setMainUrl(String mainUrl) {
        BookingHistoryFactory.mainUrl = mainUrl;
    }

    public static BookingHistoryDto createBookingHistoryDto(Booking booking) {
        return BookingHistoryDto.builder()
                .bookingId(booking.getId())
                .hotelId(booking.getHotel().getId())
                .hotelName(booking.getHotel().getName())
                .hotelStars(booking.getHotel().getStars())
                .hotelType(booking.getHotel().getHotelType().name())
                .mainPhotoUrl(mainUrl + booking.getHotel().getMainPhoto().get().getFileName())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .totalPrice(booking.getTotalPrice())
                .createDate(booking.getCreateDate())
                .roomNumber(booking.getRoom().getNumber())
                .roomDescription(booking.getRoom().getDescription())
                .bedType(booking.getRoom().getBedType().name())
                .build();
    }
}
