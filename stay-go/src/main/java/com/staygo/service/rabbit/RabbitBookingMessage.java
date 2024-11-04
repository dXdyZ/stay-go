package com.staygo.service.rabbit;

import com.staygo.enity.DTO.rabbit.ArmoredRoomDTO;
import com.staygo.enity.hotel.ArmoredRoom;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitBookingMessage implements OrderMessagingServiceInterface{
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitBookingMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendDataBooking(ArmoredRoomDTO armoredRoomDTO) {
        rabbitTemplate.convertAndSend("MessageAboutBooking", armoredRoomDTO);
    }
}
