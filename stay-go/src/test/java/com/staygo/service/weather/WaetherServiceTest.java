package com.staygo.service.weather;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class WaetherServiceTest {

    private static final Logger log = LoggerFactory.getLogger(WaetherServiceTest.class);
    @Autowired
    private WaetherService waetherService;

    @Test
    void sortedTimeByDaySuccessData() throws ParseException {
        Map<String, Integer> weatherData = waetherService.sortedTimeByDay("02.11.2024", "10.11.2024", "kaliningrad", "Russian");
    }

}