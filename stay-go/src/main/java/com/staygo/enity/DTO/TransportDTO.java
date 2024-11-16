package com.staygo.enity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportDTO {
    private String name;
    private String country;
    private String city;
    private String street;
    private String numberHouse;
}
