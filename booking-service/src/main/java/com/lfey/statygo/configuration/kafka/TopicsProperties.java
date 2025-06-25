package com.lfey.statygo.configuration.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app.topics")
public class TopicsProperties {

    @Data
    public static class TopicProperties {
        private String name;
        private Integer partition;
        private Integer replicas;
    }

    private Map<String, TopicProperties> topics = new HashMap<>();
}
