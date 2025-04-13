package com.staygo.userservice.config;

import com.staygo.userservice.component.FeignErrorDecoder;
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
        return new FeignErrorDecoder();
    }
}
