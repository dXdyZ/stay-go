package com.staygo.service;

import com.staygo.enity.address.Address;
import com.staygo.enity.address.AddressForAirport;
import com.staygo.enity.address.ResponseMapApi;
import com.staygo.component.MapAddressForLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class GenerateLinkForMap {
    private final MapAddressForLink mapAddressForLink;
    private final RestTemplate restTemplate;
    private final AirportService airportService;

    @Autowired
    public GenerateLinkForMap(MapAddressForLink mapAddressForLink, RestTemplate restTemplate, AirportService airportService) {
        this.mapAddressForLink = mapAddressForLink;
        this.restTemplate = restTemplate;
        this.airportService = airportService;
    }

    public String generateLink(String city, String country, Address address) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("User-Agent", "MyApp (myemail@example.com)");
        httpHeaders.set("Accept", "application/json");

        AddressForAirport airport = airportService.getAddressAirportByCityAndCounty(city, country);

        String hotelAddress = URLEncoder.encode(mapAddressForLink.mappingAddressForAirport(null, address.getStreet(), address.getCity(), address.getCountry(), address.getNumberHouse()),
                StandardCharsets.UTF_8);
        String airAddress = URLEncoder.encode(mapAddressForLink.mappingAddressForAirport(airport.getName(), null, airport.getCity(), airport.getCountry(), null),
                StandardCharsets.UTF_8);

        URI uriHotel;
        URI uriAir;

        try {
            uriHotel = new URI("https://nominatim.openstreetmap.org/search?q=" + hotelAddress + "&format=json&limit=1&addressdetails=1");
            uriAir = new URI("https://nominatim.openstreetmap.org/search?q=" + airAddress + "&format=json&limit=1&addressdetails=1");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<ResponseMapApi[]> responseHotel = restTemplate.exchange(
                uriHotel,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                ResponseMapApi[].class);
        ResponseEntity<ResponseMapApi[]> responseAir = restTemplate.exchange(
                uriAir,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                ResponseMapApi[].class);

        return responseAir.getBody()[0] != null && responseHotel.getBody()[0] != null ? "https://www.google.com/maps/dir/?api=1&origin=" + responseAir.getBody()[0].getLat() +"," + responseAir.getBody()[0].getLon() +
                "&destination=" + responseHotel.getBody()[0].getLat() + "," + responseHotel.getBody()[0].getLon() : null;
    }
}
