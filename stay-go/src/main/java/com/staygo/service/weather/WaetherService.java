package com.staygo.service.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.staygo.enity.weather.Weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class WaetherService {
    private final RestTemplate restTemplate;

    @Autowired
    public WaetherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HashMap<String, Double> sortedTimeByDay() {
        Weather weather =  restTemplate.getForObject("https://api.open-meteo.com/v1/forecast?latitude=54.7065&longitude=20.511&hourly=temperature_2m&start_date=2024-10-30&end_date=2024-11-06",
                Weather.class);
        List<String> time = Objects.requireNonNull(weather).getHourly().getTime();
        List<Double> weatherInDay = Objects.requireNonNull(weather).getHourly().getTemperature_2m();
        int quantityDay = time.size() / 24;
        double ma = 0;
        int day = 1;
        HashMap<String, Double> allWeather = new HashMap<>();
        for (int i = 0; i < weatherInDay.size(); i++) {
            if (i != 24) {
                ma += weatherInDay.get(i);
            } else {
                ma /= 24;
                if (allWeather.isEmpty()) {
                    allWeather.put("day: " + day, ma);
                } else  {
                    allWeather.put("day: " + (allWeather.size() + 1), ma);
                }
                for (int j = 0; j < 24; j++) {
                    weatherInDay.remove(i);
                }
                i = 0;
                ma = 0;
            }
        }
        return allWeather;
    }
}
