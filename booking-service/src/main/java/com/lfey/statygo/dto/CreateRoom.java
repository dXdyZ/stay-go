package com.lfey.statygo.dto;

import com.lfey.statygo.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoom {
    private Integer number;
    private Integer capacity;
    private Double pricePerDay;
    private String description;
    private RoomType roomType;
}
