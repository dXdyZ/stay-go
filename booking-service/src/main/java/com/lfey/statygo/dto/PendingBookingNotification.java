package com.lfey.statygo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingBookingNotification implements Serializable {
    private Long bookingId;
    private Long hotelId;
    private Long roomId;

    private String guestName;
    private String roomType;
    private Integer guests;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;

    private String bookingStatus;

    private String guestContactEmail;
    private String guestContactNumber;
}
