package com.example.sendermessagestaygo.receiver;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import com.example.sendermessagestaygo.service.SendBookingMail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private final RabbitTemplate rabbitTemplate;
    private final SendBookingMail sendBookingMail;

    @Autowired
    public Receiver(RabbitTemplate rabbitTemplate, SendBookingMail sendBookingMail) {
        this.rabbitTemplate = rabbitTemplate;
        this.sendBookingMail = sendBookingMail;
    }

    //Делает приемник пассивным
    @RabbitListener(queues = "MessageAboutBooking")
    public void receiveBookingMessage(@Payload ArmoredRoomDTO armoredRoomDTO) {
        sendBookingMail.sendMail(armoredRoomDTO);
    }
}
