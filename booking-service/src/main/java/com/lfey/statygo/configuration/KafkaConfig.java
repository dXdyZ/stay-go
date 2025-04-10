package com.lfey.statygo.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${app.topics.hotel-booking-event.name}")
    private String hotelBookingEventTopicName;

    @Value("${app.topics.hotel-booking-event.partition}")
    private Integer hotelBookingEventTopicPartition;

    @Value("${app.topics.hotel-booking-event.replicas}")
    private Integer hotelBookingEventTopicReplicas;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder
                .name(hotelBookingEventTopicName)
                .partitions(hotelBookingEventTopicPartition)
                .replicas(hotelBookingEventTopicReplicas)
                .build();
    }
}
