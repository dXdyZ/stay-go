package com.lfey.statygo.component.factory;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.PriceCalculate;
import com.lfey.statygo.dto.BookingRoom;
import com.lfey.statygo.entity.Booking;
import com.lfey.statygo.entity.Room;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class BookingFactory {
    public Booking createBooking(Room freeRoom, String username, BookingRoom bookingRoom) {
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
}
