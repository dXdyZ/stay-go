package com.staygo.service.component;

import com.staygo.enity.address.Address;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapAddressForLinkTest {


    private static final Logger log = LoggerFactory.getLogger(MapAddressForLinkTest.class);

    @Test
    void mappingAddressForAirport() {
        MapAddressForLink mapAddressForLink = new MapAddressForLink();
        String result = mapAddressForLink.mappingAddressForAirport( null, "hello", "hello", "hello", "hello");
        log.info("result the method: {}", result);
    }
}