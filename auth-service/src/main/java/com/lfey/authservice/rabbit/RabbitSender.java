package com.lfey.authservice.rabbit;

import com.lfey.authservice.dto.rabbit.RegistrationEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RabbitSender {
    private final RabbitTemplate rabbitTemplate;
    private final String queueRegistrationEvent;

    @Autowired
    public RabbitSender(RabbitTemplate rabbitTemplate,
                        @Qualifier("queueRegistrationEvent") String queueRegistrationEvent) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueRegistrationEvent = queueRegistrationEvent;
    }

    public void sendRegistrationEvent(RegistrationEvent registrationEvent) {
        rabbitTemplate.convertAndSend(queueRegistrationEvent, registrationEvent);
    }
}
