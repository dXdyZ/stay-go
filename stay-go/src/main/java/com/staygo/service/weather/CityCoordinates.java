package com.staygo.service.weather;

import com.staygo.enity.weather.Country;
import com.staygo.file_writer.FileWriterGateway;
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
    private final FileWriterGateway fileWriterGateway;

    @Autowired
    public CityCoordinates(RestTemplate restTemplate, FileWriterGateway fileWriterGateway) {
        this.restTemplate = restTemplate;
        this.fileWriterGateway = fileWriterGateway;
    }

    @Value("${staygo.coordinate.key}")
    private String apiKey;


    public Country getCoordinateByCityAndCountry(String city, String country) {
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
        return countryReturnAndSave;
    }

    public void writeFileCoordinates(Country country) {
        if (country != null) {

        }
    }
}
