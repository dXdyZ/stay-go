package com.staygo.enity.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hourly {
    private List<String> time;
    private List<Double> temperature_2m;
}
