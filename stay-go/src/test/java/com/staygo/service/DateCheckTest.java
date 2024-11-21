package com.staygo.service;

import com.staygo.custom_exception.DateException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DateCheckTest {

    private static final Logger log = LoggerFactory.getLogger(DateCheckTest.class);

    @Test
    void checkForThePresent() throws DateException, ParseException {
        DateCheck dateCheck = new DateCheck();
        boolean result = dateCheck.checkForThePresent("26.11.2024", "24.11.2024");
        assertEquals(true, result);
    }

    @Test
    void mapDateSuccessData() {
        DateCheck dateCheck = new DateCheck();
        List<String> mapData = dateCheck.mapDate("02.11.2024", "05.11.2024");
        assertEquals(mapData.get(0), "2024-11-02");
        assertEquals(mapData.get(1), "2024-11-05");
    }

    @Test
    void testCreateDateOnPattern() {
        DateCheck dateCheck = new DateCheck();
        log.info("check create date: {}", dateCheck.mapNowDateInString());
    }

    @Test
    void mapDateInThePastTense() {
        DateCheck dateCheck = new DateCheck();
        assertThrows(RuntimeException.class, () -> {
            dateCheck.mapDate("02.09.2024", "05.09.2024");
        });
    }
}