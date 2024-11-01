package com.staygo.service.weather;

import com.staygo.castom_exe.DateException;
import com.staygo.enity.weather.Country;
import com.staygo.service.DateCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.staygo.enity.weather.Weather;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
public class WaetherService {
    private final RestTemplate restTemplate;
    private final CityCoordinates cityCoordinates;
    private final DateCheck dateCheck;

    @Autowired
    public WaetherService(RestTemplate restTemplate, CityCoordinates cityCoordinates, DateCheck dateCheck) {
        this.restTemplate = restTemplate;
        this.cityCoordinates = cityCoordinates;
        this.dateCheck = dateCheck;
    }

    public Map<String, Double> sortedTimeByDay(String armoredDate, String departureDate, String city, String county) throws ParseException{
        if (weatherLimitDate(armoredDate)) {
            List<String> dateForURI = dateCheck.mapDate(armoredDate, departureDate);
            Country country = cityCoordinates.getCoordinateByCityAndCountry(city, county);
            URI uri = null;
            try {
                uri = new URI("https://api.open-meteo.com/v1/forecast?latitude=" + country.getLatitude() +
                        "&longitude=" + country.getLongitude() + "&hourly=temperature_2m&start_date=" + dateForURI.get(0) +"&end_date=" + dateForURI.get(1));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            Weather weather = restTemplate.getForObject(uri, Weather.class);
            List<String> time = Objects.requireNonNull(weather).getHourly().getTime();
            List<Double> weatherInDay = Objects.requireNonNull(weather).getHourly().getTemperature_2m();
            return calculationWeather(time, weatherInDay);
        } else return null;
    }

    private boolean weatherLimitDate(String armoredDate) {
        try {
            if (dateCheck.mapNowDateInString().equals(armoredDate) || dateCheck.differenceCalculationDate(dateCheck.mapNowDateInString(), armoredDate) <= 14) {
                return true;
            } else return false;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Double> calculationWeather(List<String> time, List<Double> weatherInDay) {
        int quantityDay = time.size() / 24;
        double ma = 0;
        int day = 1;
        Map<String, Double> allWeather = new HashMap<>();
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
