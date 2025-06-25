package com.lfey.statygo.configuration;

import com.lfey.statygo.component.factory.BookingHistoryFactory;
import com.lfey.statygo.component.factory.PhotoDtoFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticPropertyLoader {

    @Value("${app.main-url}")
    private String mainUrl;

    @PostConstruct
    public void init() {
        BookingHistoryFactory.setMainUrl(mainUrl);
        PhotoDtoFactory.setMainUrl(mainUrl);
    }
}
