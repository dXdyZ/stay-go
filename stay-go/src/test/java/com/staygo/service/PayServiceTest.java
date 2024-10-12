package com.staygo.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class PayServiceTest {

    @Test
    void costCalculation() throws ParseException {
        String a = "123123", b = "1231123";
        if (!a.equals(b)) {
            System.out.println(true);
        } else System.out.println(false);
    }
}