package com.staygo.service.rabbit;

import com.staygo.enity.hotel.ArmoredRoom;
import com.staygo.enity.hotel.Room;
import com.staygo.enity.user.Users;
import com.staygo.service.hotel_ser.ArmoredRoomService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RabbitBookingMessageTest {

    @Autowired
    private RabbitBookingMessage rabbitBookingMessage;

    @Autowired
    private ArmoredRoomService armoredRoomService;

    private Principal principal;

    @BeforeAll
    void init() {
        principal = () -> "testController";
    }

    @Test
    void sendDataBooking() {
        ArmoredRoom armoredRoom = ArmoredRoom.builder()
                .dateArmored("12.12.1232")
                .createDate(new Date())
                .room(Room.builder()
                        .roomName("001")
                        .prestige("president")
                        .price(BigDecimal.valueOf(12300.00))
                        .build())
                .departureDate("14.12.1232")
                .users(Users.builder()
                        .username("testController")
                        .email("ttttilinn85@gmail.com")
                        .build())
                                                .build();
        rabbitBookingMessage.sendDataBooking(armoredRoomService.createArmoredDTO(armoredRoom, "Gorkogo", "kaliningrad", "Test hotel for the method"));
    }
}