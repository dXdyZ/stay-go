package com.staygo.service.rabbit;

import com.staygo.enity.DTO.rabbit.ArmoredRoomDTO;
import com.staygo.enity.DTO.rabbit.CarReservationDTO;
import com.staygo.enity.DTO.rabbit.UserFindHotelDTO;
import com.staygo.enity.DTO.rabbit.UserRegCodeDTO;
import com.staygo.enity.weather.Country;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Setter
@Service
public class RabbitMessage {
    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.name.booking}")
    private String messageAboutBooking;

    @Value("${queue.name.verefEmail}")
    private String verefEmail;

    @Value("${queue.name.carDataEmail}")
    private String messageCarReservation;

    @Value("${queue.name.userFindHotel}")
    private String userFindHotel;

    @Autowired
    public RabbitMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDataBooking(ArmoredRoomDTO armoredRoomDTO) {
        rabbitTemplate.convertAndSend(messageAboutBooking, armoredRoomDTO);
    }

    public void sendDataUserFindHotel(UserFindHotelDTO message) {
        log.info("data user find hotel: {}", userFindHotel);
        rabbitTemplate.convertAndSend(userFindHotel, message);
    }

    public void sendDataReservationCar(CarReservationDTO carDTO) {
        rabbitTemplate.convertAndSend(messageCarReservation, carDTO);
    }

    public void sendDataCodeForUserAuth(UserRegCodeDTO userRegCodeDTO) {
        rabbitTemplate.convertAndSend(verefEmail, userRegCodeDTO);
    }
}
