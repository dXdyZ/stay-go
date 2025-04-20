package com.lfey.authservice.service.clients;

import com.lfey.authservice.exception.CustomNetworkException;
import com.lfey.authservice.exception.ServerErrorException;
import feign.FeignException;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserClientConfig {

    //TODO Add collecting logs
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            if (response == null) {
                return new CustomNetworkException();
            }
            if (response.status() >= 500) {
                return new ServerErrorException("Server error");
            }
            return FeignException.errorStatus(methodKey, response);
        };
    }
}






