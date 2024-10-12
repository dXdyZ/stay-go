package com.staygo.service;

import com.staygo.castom_exe.DateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class PayService {

    public BigDecimal costCalculation(String armoredDare, String departureDate, BigDecimal price) throws ParseException, DateException {
        if (!armoredDare.equals(departureDate)) {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            Date armorDate = format.parse(armoredDare);
            Date deparDate = format.parse(departureDate);
            if (checkForThePresent(deparDate, armorDate)) {
                long millis = deparDate.getTime() - armorDate.getTime();
                int date = (int) (millis / (24 * 60 * 60 * 1000));
                BigDecimal priceDate = BigDecimal.valueOf(date);
                return price.multiply(priceDate);
            } else {
                throw new DateException("Даты не могут быть в прошлом");
            }
        } else {
            return price;
        }
    }

    private boolean checkForThePresent(Date departureDate, Date armoredDate) {
        Date presentDate = new Date();
        if (departureDate.getTime() > presentDate.getTime() && armoredDate.getTime() > presentDate.getTime()) {
            return true;
        } else return false;
    }
}
