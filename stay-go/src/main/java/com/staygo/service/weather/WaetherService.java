package com.staygo.service.weather;

import com.staygo.enity.weather.Country;
import com.staygo.enity.weather.Weather;
import com.staygo.service.DateCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

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

    public Map<String, Integer> sortedTimeByDay(String armoredDate, String departureDate, String city, String county) throws ParseException{
        if (weatherLimitDate(armoredDate, departureDate)) {
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
            List<Integer> weatherInDay = Objects.requireNonNull(weather).getHourly().getTemperature_2m().stream().map(Integer::valueOf).collect(Collectors.toCollection(ArrayList::new));
            return calculationWeather(weatherInDay);
        } else return null;
    }

    private boolean weatherLimitDate(String armoredDate, String departureDate) {
        return dateCheck.mapNowDateInString().equals(armoredDate) || dateCheck.differenceCalculationDate(dateCheck.mapNowDateInString(), armoredDate) <= 14 &&
                dateCheck.differenceCalculationDate(dateCheck.mapNowDateInString(), departureDate) <= 14;
    }

    private Map<String, Integer> calculationWeather(List<Integer> weatherInDay) {
        double ma = 0;
        int day = 1;
        Map<String, Integer> allWeather = new HashMap<>();
        for (int i = 0; i < weatherInDay.size(); i++) {
            if (i != 24) {
                ma += weatherInDay.get(i);
            } else {
                ma /= 24;
                if (allWeather.isEmpty()) {
                    allWeather.put("day: " + day, (int) ma);
                } else  {
                    allWeather.put("day: " + (allWeather.size() + 1), (int) ma);
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
