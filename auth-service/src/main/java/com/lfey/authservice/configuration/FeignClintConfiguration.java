package com.lfey.authservice.configuration;

import com.lfey.authservice.exception.CustomNetworkException;
import com.lfey.authservice.exception.ServerErrorException;
import feign.FeignException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClintConfiguration {
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                1000,
                5000,
                5
        );
    }


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
