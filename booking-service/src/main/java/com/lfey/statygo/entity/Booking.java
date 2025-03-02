package com.lfey.statygo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "bookings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private Instant startDate;

    private Instant endDate;

    private Boolean totalPrice;

    private Instant createDate;

    @LastModifiedDate
    private Instant updateDate;
}
