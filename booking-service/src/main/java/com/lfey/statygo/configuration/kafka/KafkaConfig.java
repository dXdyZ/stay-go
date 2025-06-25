package com.lfey.statygo.configuration.kafka;

import com.lfey.statygo.dto.BookingDetailsEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {
    private final TopicsProperties topicsProperties;

    public KafkaConfig(TopicsProperties topicsProperties) {
        this.topicsProperties = topicsProperties;
    }

    @Bean
    public List<NewTopic> newTopics() {
        return topicsProperties.getTopics().values().stream()
                .map(topic -> TopicBuilder.name(topic.getName())
                        .partitions(topic.getPartition())
                        .replicas(topic.getReplicas())
                        .build())
                .toList();
    }
}
