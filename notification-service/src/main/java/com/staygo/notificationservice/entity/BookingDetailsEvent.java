package com.staygo.notificationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailsEvent {
    private Long bookingId;
    private String hotelName;
    private String hotelAddress;
    private String startDate;
    private String endDate;
    private String roomType;
    private UUID userPublicId;
    private Double totalPrice;
    private String bookingStatus;
    private Integer reservedRooms;

    public String toPrettyString() {
        return "Booking Details:\n" +
                "-------------------------\n" +
                "Booking ID: " + bookingId + "\n" +
                "Hotel Name: " + hotelName + "\n" +
                "Hotel Address: " + hotelAddress + "\n" +
                "Check-In Date: " + startDate + "\n" +
                "Check-Out Date: " + endDate + "\n" +
                "Room Type: " + roomType + "\n" +
                "Booked By: " + userPublicId + "\n" +
                "Total Price: $" + totalPrice + "\n" +
                "Booking Status: " + bookingStatus + "\n" +
                "Reserved Rooms: " + reservedRooms + "\n" +
                "-------------------------";
    }
}