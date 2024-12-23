package com.staygo.service.new_test;


import com.staygo.custom_exception.DateException;
import com.staygo.service.PayService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PayServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PayServiceTest.class);
    @Autowired
    private PayService payService;

    @Test
    void allValidDataTest() {
        assertEquals(BigDecimal.valueOf(46L), payService.costCalculation("01.12.2024", "03.12.2024", BigDecimal.valueOf(23L)));
    }

    @Test
    void allValidDataInOneDay() {
        assertEquals(BigDecimal.valueOf(23L), payService.costCalculation("01.12.2024", "01.12.2024", BigDecimal.valueOf(23L)));
    }

    @Test
    void dateInThePast() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> payService.costCalculation("29.11.2023", "30.11.2024", BigDecimal.valueOf(23L))
        );

        // Проверяем текст внутри исключения
        Throwable cause = exception.getCause();
        assertEquals(DateException.class, cause.getClass());
        assertEquals("Даты не могут быть в прошлом", cause.getMessage());
    }
}
