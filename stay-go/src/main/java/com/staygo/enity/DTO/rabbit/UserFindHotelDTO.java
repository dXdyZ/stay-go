package com.staygo.enity.DTO.rabbit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFindHotelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String city;
    private String country;
    private String reservationDate;
    private String departureDate;
    private Integer grade;
    private String username;
}
