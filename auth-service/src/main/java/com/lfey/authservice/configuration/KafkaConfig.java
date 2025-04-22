package com.lfey.authservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.topics.email-verification.name}")
    private String emailVerificationTopicName;

    @Value("${app.topics.email-verification.partitions}")
    private Integer emailVerificationTopicPartitions;

    @Value("${app.topics.email-verification.replicas}")
    private Integer emailVerificationTopicReplicas;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder
                .name(emailVerificationTopicName)
                .partitions(emailVerificationTopicPartitions)
                .replicas(emailVerificationTopicReplicas)
                .build();
    }
}
