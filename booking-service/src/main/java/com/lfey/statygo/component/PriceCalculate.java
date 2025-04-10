package com.lfey.statygo.component;

import java.time.LocalDate;

public final class PriceCalculate {
    public static Double calculationTotalPrice(Double price, LocalDate startDate, LocalDate endDate) {
        return CustomDateFormatter.getNumberOfDays(startDate, endDate) * price;
    }
}
