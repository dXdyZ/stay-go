package com.example.sendermessagestaygo.receiver;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public Receiver(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public ArmoredRoomDTO receiveBookingMessage() {
        return rabbitTemplate.receiveAndConvert("MessageAboutBooking",
                new ParameterizedTypeReference<ArmoredRoomDTO>() {});
    }
}
