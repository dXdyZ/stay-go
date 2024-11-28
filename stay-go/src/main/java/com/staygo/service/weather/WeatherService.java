package com.staygo.service.weather;

import com.staygo.enity.weather.Country;
import com.staygo.enity.weather.Weather;
import com.staygo.service.DateCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private final CityCoordinates cityCoordinates;
    private final DateCheck dateCheck;

    @Autowired
    public WeatherService(RestTemplate restTemplate, CityCoordinates cityCoordinates, DateCheck dateCheck) {
        this.restTemplate = restTemplate;
        this.cityCoordinates = cityCoordinates;
        this.dateCheck = dateCheck;
    }

    @Cacheable(value = "weatherValue", key = "#armoredDate + '-' + #departureDate + '-' + #city + '-' + #county")
    public Map<String, Integer> sortedTimeByDay(String armoredDate, String departureDate, String city, String county){
        if (weatherLimitDate(armoredDate, departureDate)) {
            List<String> dateForURI = dateCheck.mapDate(armoredDate, departureDate);
            Country country = cityCoordinates.getCoordinateByCityAndCountry(city, county);
            URI uri;
            try {
                uri = new URI("https://api.open-meteo.com/v1/forecast?latitude=" + country.getLatitude() +
                        "&longitude=" + country.getLongitude() + "&hourly=temperature_2m&start_date=" + dateForURI.get(0) +"&end_date=" + dateForURI.get(1));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            log.info("weather uri: {}", uri);
            Weather weather = restTemplate.getForObject(uri, Weather.class);;
            List<Integer> weatherInDay = Objects.requireNonNull(weather).getHourly().getTemperature_2m().stream().collect(Collectors.toCollection(ArrayList::new));
            log.info("weather in day: {}", weatherInDay);
            return calculationWeather(weatherInDay);
        } else return Map.of("unknown", 0);
    }

    private boolean weatherLimitDate(String armoredDate, String departureDate) {
        return dateCheck.mapNowDateInString().equals(armoredDate) || dateCheck.differenceCalculationDate(dateCheck.mapNowDateInString(), armoredDate) <= 14 &&
                dateCheck.differenceCalculationDate(dateCheck.mapNowDateInString(), departureDate) <= 14;
    }

    private Map<String, Integer> calculationWeather(List<Integer> weatherInDay) {
        double ma = 0;
        int day = 1;
        log.info("inner data: {}", weatherInDay);
        Map<String, Integer> allWeather = new HashMap<>();
        for (int i = 0; i < weatherInDay.size(); i++) {
            ma += weatherInDay.get(i);
            // Если обработано 24 часа или достигнут конец списка
            if ((i + 1) % 24 == 0 || i == weatherInDay.size() - 1) {
                ma /= 24; // Среднее значение за день
                allWeather.put("day: " + day++, (int) ma);
                ma = 0; // Сброс среднего
            }
        }
        log.info("all calculation weather: {}", allWeather);
        return allWeather;
    }
}
