package com.staygo.enity.weather;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    private Hourly hourly;
}


