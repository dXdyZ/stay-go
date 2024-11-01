package com.staygo.enity.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private String name;
    private Double latitude;
    private Double longitude;
    private String country;
    private String state;
}
