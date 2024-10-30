package com.staygo.service.weather;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class WaetherServiceTest {

    private static final Logger log = LoggerFactory.getLogger(WaetherServiceTest.class);
    @Autowired
    private WaetherService waetherService;

    @Test
    void test() {
        log.info("result request: {}", waetherService.sortedTimeByDay());
    }

    @Test
    void testSub() {
        String clock = "2024-10-30T00:00";
        int clockTimeIndex = clock.lastIndexOf('T');
        String index = clock.substring(clockTimeIndex + 1);
        log.info("sud string: {}", index);
        log.info("sud string: {}", clockTimeIndex);
    }
}