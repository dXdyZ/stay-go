package com.staygo.service.new_test;

import com.staygo.custom_exception.DateException;
import com.staygo.service.DateCheck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DateCheckTest {

    @Autowired
    private DateCheck dateCheck;

    @Test
    void validDataInMethodCheckForThePresent() throws DateException {
        assertEquals(true, dateCheck.checkForThePresent("02.12.2024", "01.12.2024"));
    }

    @Test
    void datesInThePastInMethodCheckForThePresent() {
        DateException dateException = assertThrows(DateException.class,
                () -> dateCheck.checkForThePresent("29.11.2024", "30.11.2024"));

        assertEquals("Даты не могут быть в прошлом", dateException.getMessage());
    }

    @Test
    void validDataInMethodDifferenceCalculationDate() {
        assertEquals(2, dateCheck.differenceCalculationDate("01.12.2024", "03.12.2024"));
    }
}







