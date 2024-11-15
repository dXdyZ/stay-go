package com.example.sendermessagestaygo.receiver;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import com.example.sendermessagestaygo.enity.UserRegCodeDTO;
import com.example.sendermessagestaygo.service.SendMailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private final SendMailService sendBookingMail;


    @Autowired
    public Receiver( SendMailService sendBookingMail) {
        this.sendBookingMail = sendBookingMail;
    }

    //Делает приемник пассивным
    @RabbitListener(queues = "MessageAboutBooking")
    public void receiveBookingMessage(@Payload ArmoredRoomDTO armoredRoomDTO) {
        sendBookingMail.sendMailForBooking(armoredRoomDTO);
    }

    @RabbitListener(queues = "MassageAboutCodeForUser")
    public void receiveCodeForUserAuthMessage(@Payload UserRegCodeDTO userRegCodeDTO) {
        sendBookingMail.sendMailCodeForUserAuth(userRegCodeDTO);
    }
}
