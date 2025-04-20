package com.lfey.authservice.kafka;

import com.lfey.authservice.dto.rabbit.RegistrationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, RegistrationEvent> template;

    @Value("${spring.kafka.topic.email-verification.name}")
    private String topicName;

    public KafkaProducer(KafkaTemplate<String, RegistrationEvent> template) {
        this.template = template;
    }

    public void sendRegistrationEvent(RegistrationEvent registrationEvent) {
        template.send(topicName, registrationEvent.getEmail() + "|" + registrationEvent.getEventType().name(),
                registrationEvent);
    }
}






