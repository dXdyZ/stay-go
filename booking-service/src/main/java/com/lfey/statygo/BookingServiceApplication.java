package com.lfey.statygo;

import com.lfey.statygo.configuration.kafka.TopicsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(TopicsProperties.class)
@EnableJpaRepositories(basePackages = "com.lfey.statygo.repository.jpaRepository")
@EnableRedisRepositories(basePackages = "com.lfey.statygo.repository.redisRepository")
public class BookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }

}
