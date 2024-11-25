package com.staygo.service.rabbit;

import com.staygo.enity.DTO.rabbit.UserFindHotelDTO;
import com.staygo.enity.DTO.rabbit.UserRegCodeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RabbitMessageTest {

    @Autowired
    private RabbitMessage rabbitMessage;

    @Test
    void sendDataUserFindHotel() {
        rabbitMessage.sendDataUserFindHotel(new UserFindHotelDTO("test", "test", "test", "test", 4, "test"));
    }

    @Test
    void sendDataCodeForUserAuth() {
        rabbitMessage.sendDataCodeForUserAuth(new UserRegCodeDTO(12312, "test@email.com"));
    }
}