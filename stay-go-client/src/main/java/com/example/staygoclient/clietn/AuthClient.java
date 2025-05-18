package com.example.staygoclient.clietn;

import com.example.staygoclient.dto.*;
import com.example.staygoclient.exception.ApiErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Component
public class AuthClient {

    private static final String MAIN_URL = "http://localhost:9191/api/auth";

    private final RestTemplate restTemplate;

    public AuthClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void registration(RegistrationDto registrationDto) {
        try {
            restTemplate.postForEntity(
                    MAIN_URL + "/registration", registrationDto, ErrorResponse.class
            );
        } catch (HttpClientErrorException exception) {
            throw new ApiErrorException(exception.getResponseBodyAs(ErrorResponse.class));
        }
    }

    public JwtTokenDto registrationConfirm(ConfirmRegistrationDto confirmRegistrationDto) {
        try {
            ResponseEntity<JwtTokenDto> jwtTokenDto = restTemplate.postForEntity(
                    MAIN_URL + "/registration/confirm", confirmRegistrationDto, JwtTokenDto.class
            );
            if (jwtTokenDto.getBody() != null) {
                return jwtTokenDto.getBody();
            } else {
                throw new ApiErrorException(new ErrorResponse(
                        Instant.now(),
                        Map.of("message", "Server error"),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
            }
        } catch (HttpClientErrorException exception) {
            throw new ApiErrorException(exception.getResponseBodyAs(ErrorResponse.class));
        }
    }

    public JwtTokenDto login(LoginDto loginDto) {
        try {
            ResponseEntity<JwtTokenDto> jwtTokenDto = restTemplate.postForEntity(
                    MAIN_URL + "/login", loginDto, JwtTokenDto.class
            );
            if (jwtTokenDto.getBody() != null){
                return jwtTokenDto.getBody();
            } else {
                throw new ApiErrorException(new ErrorResponse(
                        Instant.now(),
                        Map.of("message", "Server error"),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
            }
        } catch (HttpClientErrorException exception) {
            throw new ApiErrorException(exception.getResponseBodyAs(ErrorResponse.class));
        }
    }
}
