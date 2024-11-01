package com.staygo.service.weather;

import com.staygo.enity.weather.Country;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CityCoordinatesTest {

    private static final Logger log = LoggerFactory.getLogger(CityCoordinatesTest.class);
    @Autowired
    private CityCoordinates cityCoordinates;

    @Test
    void getCoordinateByCityAndCountry() throws URISyntaxException {

        Country countries = cityCoordinates.getCoordinateByCityAndCountry("Moscow", "Russia");


        log.info("request result: {}", countries.toString());
    }
}