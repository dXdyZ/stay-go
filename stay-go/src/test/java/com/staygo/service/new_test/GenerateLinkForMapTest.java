package com.staygo.service.new_test;

import com.staygo.enity.address.Address;
import com.staygo.enity.address.AddressForAirport;
import com.staygo.service.AirportService;
import com.staygo.service.GenerateLinkForMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenerateLinkForMapTest {


    private static final Logger log = LoggerFactory.getLogger(GenerateLinkForMapTest.class);

    @Autowired
    private GenerateLinkForMap generateLinkForMap;

    @MockBean
    private AirportService airportService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestCorrectData() {
        String city = "Калининград";
        String country = "Россия";
        AddressForAirport airport = AddressForAirport.builder()
                .id(1L)
                .name("Храброво")
                .city("Калининград")
                .country("Россия")
                .build();
        Address address = Address.builder()
                .country(country)
                .street("Кутаисская")
                .numberHouse("42")
                .city(city)
                .build();
        when(airportService.getAddressAirportByCityAndCounty(city, country)).thenReturn(airport);
        String result = generateLinkForMap.generateLink(city, country, address);
        log.info("test na null: {}", result);
    }

    @Test
    void testEncode() {
        log.info("encode: {}", URLEncoder.encode("Храброво+Калининград"), StandardCharsets.UTF_8);
    }
}
















