package com.staygo.service.hotel_ser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArmoredRoomTest {

    @Autowired
    private ArmoredRoomService armoredRoomService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomService roomService;

    private Principal principal;

    @BeforeAll
    void init() {
        principal = () -> "testUser";
    }


}
