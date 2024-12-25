package com.example.sendermessagestaygo.receiver;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import com.example.sendermessagestaygo.enity.CarReservationDTO;
import com.example.sendermessagestaygo.enity.UserFindHotelDTO;
import com.example.sendermessagestaygo.enity.UserRegCodeDTO;
import com.example.sendermessagestaygo.itegration.FileWriterGateway;
import com.example.sendermessagestaygo.service.SendMailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private final SendMailService sendBookingMail;
    private final FileWriterGateway fileWriterGateway;


    @Autowired
    public Receiver(SendMailService sendBookingMail, FileWriterGateway fileWriterGateway) {
        this.sendBookingMail = sendBookingMail;
        this.fileWriterGateway = fileWriterGateway;
    }

    //Делает приемник пассивным
    @RabbitListener(queues = "MessageAboutBooking")
    public void receiveBookingMessage(@Payload ArmoredRoomDTO armoredRoomDTO) {
        sendBookingMail.sendMailForBooking(armoredRoomDTO);
        fileWriterGateway.writeToFile("log-reservation-room.json", armoredRoomDTO);
    }

    @RabbitListener(queues = "UserFindHotel")
    public void receiveUserFindHotelMessage(@Payload UserFindHotelDTO message) {
        fileWriterGateway.writeToFile("log-find-user-hotel.json", message);
    }

    @RabbitListener(queues = "MassageAboutCodeForUser")
    public void receiveCodeForUserAuthMessage(@Payload UserRegCodeDTO userRegCodeDTO) {
        sendBookingMail.sendMailCodeForUserAuth(userRegCodeDTO);
    }

    @RabbitListener(queues = "MessageCarReservation")
    public void receiveCarReservation(@Payload CarReservationDTO carReservationDTO) {
        sendBookingMail.sendMailCarReservation(carReservationDTO);
        fileWriterGateway.writeToFile("log-reservation-car.json", carReservationDTO);
    }
}
