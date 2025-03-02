package com.lfey.statygo.component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormater {
    public static Instant dateFormater(String date) throws DateTimeParseException{
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            return LocalDate.parse(date, dateTimeFormatter).atStartOfDay(ZoneId.of("UTC")).toInstant();
        } catch (DateTimeParseException e) {
            throw e;
        }
    }
}
