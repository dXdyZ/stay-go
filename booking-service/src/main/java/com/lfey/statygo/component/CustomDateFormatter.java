package com.lfey.statygo.component;

import com.lfey.statygo.exception.InvalidDateException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public final class CustomDateFormatter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE;

    public static Instant instantDateFormatter(String date) throws InvalidDateException{
        try {
            return LocalDate.parse(date, DATE_TIME_FORMATTER).atStartOfDay(ZoneId.of("UTC")).toInstant();
        } catch (DateTimeParseException exception) {
            throw new InvalidDateException(exception.getMessage());
        }
    }
    public static LocalDate localDateFormatter(String date) throws InvalidDateException{
        try {
            LocalDate formattedDate = LocalDate.parse(date, DATE_TIME_FORMATTER);
            if (formattedDate.isBefore(LocalDate.now())) throw new InvalidDateException("Date must be in the future");
            return formattedDate;
        } catch (DateTimeParseException exception) {
            throw new InvalidDateException("Date must be in the format: YYYY-MM-DD");
        }
    }

    public static void dateVerification(String startDate, String endDate) throws InvalidDateException{
        if (localDateFormatter(startDate).isAfter(localDateFormatter(endDate)))
            throw new InvalidDateException("Start date must be more end date");
    }

    public static int getNumberOfDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }
}






