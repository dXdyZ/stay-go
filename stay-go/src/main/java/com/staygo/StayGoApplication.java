package com.staygo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching //Включает кеширование Spring
public class StayGoApplication {

    public static void main(String[] args) {
        SpringApplication.run(StayGoApplication.class, args);
    }

}
