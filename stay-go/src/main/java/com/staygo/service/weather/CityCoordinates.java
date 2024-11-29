package com.staygo.service.weather;

import com.staygo.enity.weather.Country;
import com.staygo.service.country.CountryService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Setter
@Component
public class CityCoordinates {

    private final RestTemplate restTemplate;
    private final CountryService countryService;

    @Autowired
    public CityCoordinates(RestTemplate restTemplate, CountryService countryService) {
        this.restTemplate = restTemplate;
        this.countryService = countryService;
    }

    @Value("${staygo.coordinate.key}")
    private String apiKey;


    public Country getCoordinateByCityAndCountry(String city, String country) {
        Country receivedCountry = countryService.getByName(city);
        if (receivedCountry == null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("X-Api-Key", apiKey);
            URI uri;
            try {
                uri = new URI("https://api.api-ninjas.com/v1/geocoding?city=" + city + "&country=" + country);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            Country[] countries = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(httpHeaders), Country[].class).getBody();

            Country countryReturnAndSave = Country.builder()
                    .name(countries[0].getName())
                    .latitude(countries[0].getLatitude())
                    .longitude(countries[0].getLongitude())
                    .country(countries[0].getCountry())
                    .state(countries[0].getState())
                    .build();
            countryService.saveCountry(countryReturnAndSave);
            return countryReturnAndSave;
        } else return receivedCountry;
    }
}
