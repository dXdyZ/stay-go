package com.lfey.statygo.component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class CustomDateFormatter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE;

    public static Instant instantDateFormatter(String date) throws DateTimeParseException{
        try {
            return LocalDate.parse(date, DATE_TIME_FORMATTER).atStartOfDay(ZoneId.of("UTC")).toInstant();
        } catch (DateTimeParseException e) {
            throw e;
        }
    }
    //TODO Добавить обработку исключения неправильного ввода даты пользователем
    public static LocalDate localDateFormatter(String date) {
        return LocalDate.parse(date, DATE_TIME_FORMATTER);
    }

    public static int getNumberOfDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }
}






