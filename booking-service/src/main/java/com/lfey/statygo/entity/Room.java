package com.lfey.statygo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "rooms")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer number;

    private Integer capacity;

    private Double pricePerDay;

    private String description;

    private Boolean isActive;

    private BedType bedType;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @ToString.Exclude //Исключает поле из toString()
    private Hotel hotel;

    private Boolean autoApprove;
}
