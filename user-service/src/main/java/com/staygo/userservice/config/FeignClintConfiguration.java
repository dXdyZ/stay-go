package com.staygo.userservice.config;

import com.staygo.userservice.exception.CustomNetworkException;
import com.staygo.userservice.exception.ServerErrorException;
import feign.FeignException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClintConfiguration {

    //Settings for feign retry
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                1000,
                5000,
                5
        );
    }

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
