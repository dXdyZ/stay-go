package com.staygo.service;

import com.staygo.castom_exe.DateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class PayService {

    private final DateCheck dateCheck;

    @Autowired
    public PayService(DateCheck dateCheck) {
        this.dateCheck = dateCheck;
    }

    public BigDecimal costCalculation(String armoredDare, String departureDate, BigDecimal price){
        if (!armoredDare.equals(departureDate)) {
            BigDecimal priceDate = BigDecimal.valueOf(dateCheck.differenceCalculationDate(armoredDare, departureDate));
            return price.multiply(priceDate);
        } else {
            return price;
        }
    }
}
