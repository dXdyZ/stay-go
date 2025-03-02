package com.lfey.statygo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    private RoomType roomType;
}
