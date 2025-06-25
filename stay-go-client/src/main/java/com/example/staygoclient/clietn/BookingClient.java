package com.example.staygoclient.clietn;

import com.example.staygoclient.dto.BookingRoomDto;
import com.example.staygoclient.dto.ErrorResponse;
import com.example.staygoclient.exception.ApiErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class BookingClient {
    private static final String MAIN_URL = "http://localhost:9393/api/bookings";

    private final RestTemplate restTemplate;

    public BookingClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void booking(List<BookingRoomDto> bookings) {
        bookings.forEach(booking -> {
            try {
                restTemplate.postForEntity(MAIN_URL, BookingRoomDto.class, Void.class);
            } catch (HttpClientErrorException | HttpServerErrorException exception) {
                throw new ApiErrorException(new ErrorResponse(
                        Instant.now(),
                        Map.of("message", "Server error"),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
            }
        });
    }
}
