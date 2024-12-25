package com.staygo.service;

import com.staygo.custom_exception.DateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class DateCheck {

    private final String pattern = "dd.MM.yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public boolean checkForThePresent(String departureDate, String armoredDate) throws DateException {
        Date armorDate;
        Date deparDate;
        Date presentDate;
        try {
            armorDate = simpleDateFormat.parse(armoredDate);
            deparDate = simpleDateFormat.parse(departureDate);
            presentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date())); // Сброс миллисекунд
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (!armorDate.before(presentDate) && !deparDate.before(presentDate)) {
            return true;
        } else {
            throw new DateException("Даты не могут быть в прошлом");
        }
    }

    public String mapNowDateInString() {
        Date dateForMap = new Date();
        return simpleDateFormat.format(dateForMap);
    }

    public Integer differenceCalculationDate(String startDate, String finishDate) {
        Date armorDate;
        Date deparDate;
        try {
            armorDate = simpleDateFormat.parse(startDate);
            deparDate = simpleDateFormat.parse(finishDate);
            if (checkForThePresent(finishDate, startDate)) {
                long millis = deparDate.getTime() - armorDate.getTime();
                return  (int) (millis / (24 * 60 * 60 * 1000));
            } else {
                throw new DateException("Даты не могут быть в прошлом");
            }
        } catch (DateException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> mapDate(String startDate, String finishDate) {
        try {
            checkForThePresent(finishDate, startDate);
            Date armoredDateParse = simpleDateFormat.parse(startDate);
            Date departureDateParse = simpleDateFormat.parse(finishDate);
            SimpleDateFormat mappingDate = new SimpleDateFormat("yyyy-MM-dd");
            return new ArrayList<>() {{
                add(mappingDate.format(armoredDateParse));
                add(mappingDate.format(departureDateParse));
            }};
        } catch (ParseException | DateException e) {
            throw new RuntimeException(e);
        }
    }
}
