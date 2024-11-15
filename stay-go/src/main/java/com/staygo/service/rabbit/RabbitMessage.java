package com.staygo.service.rabbit;

import com.staygo.enity.DTO.rabbit.ArmoredRoomDTO;
import com.staygo.enity.DTO.rabbit.UserRegCodeDTO;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Setter
@Service
public class RabbitMessage {
    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.name.booking}")
    private String messageAboutBooking;

    @Value("${queue.name.verefEmail}")
    private String verefEmail;

    @Autowired
    public RabbitMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDataBooking(ArmoredRoomDTO armoredRoomDTO) {
        rabbitTemplate.convertAndSend(messageAboutBooking, armoredRoomDTO);
    }

    public void sendDataCodeForUserAuth(UserRegCodeDTO userRegCodeDTO) {
        rabbitTemplate.convertAndSend(verefEmail, userRegCodeDTO);
    }
}
