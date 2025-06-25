package com.lfey.statygo.component.factory;

import com.lfey.statygo.dto.PendingBookingNotification;
import com.lfey.statygo.entity.Booking;

public class PendingBookingNotificationFactory {
    public static PendingBookingNotification createPendingBookingNotification(Booking booking) {
        return PendingBookingNotification.builder()
                .bookingId(booking.getId())
                .hotelId(booking.getHotel().getId())
                .roomId(booking.getRoom().getId())
                .guestName(booking.getUsername())
                .roomType(booking.getRoom().getRoomType().name())
                .guests(booking.getGuests())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .totalPrice(booking.getTotalPrice())
                .bookingStatus(booking.getBookingStatus().name())
                .build();
    }
}
