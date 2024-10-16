package com.staygo.enity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArmoredHotelForUserDTO {
    private String username;
    private String armoredDate;
    private String departureDate;
    private String price;
    private String country;
    private String city;
    private String street;
}
