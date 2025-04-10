package com.lfey.statygo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateHotel {
    private String name;
    private Integer stars;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String description;
    private Long postalCode;
}
