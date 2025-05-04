package com.lfey.authservice.configuration;

import feign.Retryer;
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
}
