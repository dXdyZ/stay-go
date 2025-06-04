package com.example.staygoclient.clietn;

import com.example.staygoclient.dto.ErrorResponse;
import com.example.staygoclient.dto.HotelDto;
import com.example.staygoclient.dto.PageResponse;
import com.example.staygoclient.exception.ApiErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Map;

@Component
public class HotelClient {
    private static final String MAIN_URL = "http://localhost:9393/api/hotels";

    private final RestTemplate restTemplate;

    public HotelClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PageResponse<HotelDto> searchHotel(String startDate, String endDate,
                                              String country, String city,
                                              Integer stars, int page) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(MAIN_URL)
                .path("/search")
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("country", country)
                .queryParam("city", city)
                .queryParam("page", page);
        if (stars != null) builder.queryParam("stars", stars);
        try {
            return restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PageResponse<HotelDto>>() {}
            ).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            throw new ApiErrorException(new ErrorResponse(
                    Instant.now(),
                    Map.of("message", "Server error"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }

    public HotelDto getHotelDetails(Long hotelId, Integer guests, String startDate, String endDate) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(MAIN_URL)
                .path("/" + hotelId)
                .path("/" + guests)
                .path("/" + startDate)
                .path("/" + endDate);
        try {
            return restTemplate.getForEntity(builder.toUriString(), HotelDto.class).getBody();
        } catch (HttpClientErrorException exception) {
            throw new ApiErrorException(exception.getResponseBodyAs(ErrorResponse.class));
        }
    }
}
