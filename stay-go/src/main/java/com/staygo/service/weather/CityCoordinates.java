package com.staygo.service.weather;

import com.staygo.enity.weather.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class CityCoordinates {

    private final RestTemplate restTemplate;

    @Autowired
    public CityCoordinates(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public Country getCoordinateByCityAndCountry(String city, String country) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Api-Key", "T2p8J4QroP8RWv+oB6DDQQ==aBvUUSRKGyNvz973");
        URI uri = null;
        try {
            uri = new URI("https://api.api-ninjas.com/v1/geocoding?city=" + city + "&country=" + country);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Country[] countries = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(httpHeaders), Country[].class).getBody();

        return Country.builder()
                .name(countries[0].getName())
                .latitude(countries[0].getLatitude())
                .longitude(countries[0].getLongitude())
                .country(countries[0].getCountry())
                .state(countries[0].getState())
                .build();
    }
}
