package com.staygo.service.hotel_ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.enity.Address;
import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.Room;
import com.staygo.enity.user.Role;
import com.staygo.enity.user.Users;
import com.staygo.repository.hotel_repo.HotelRepository;
import com.staygo.repository.hotel_repo.RoomRepository;
import com.staygo.repository.user_repo.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomServiceTest {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomService roomService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Principal principal;

    @BeforeAll
    void init() {
        principal = () -> "testUser";
    }

//    @BeforeAll
//    @Transactional
//    void setUp() throws JsonProcessingException {
////        hotelRepository.deleteAll();
////        userRepository.deleteAll();
////        roomRepository.deleteAll();
//
//        Users users = Users.builder()
//                .username("testUser")
//                .email("test@example.com")
//                .password("password")
//                .build();
//        userRepository.save(users);
//    }


    @Test
    @Transactional
    @Rollback(false)
    void addedARoomToTheHotel() {
        Room room = Room.builder()
                .roomName("004")
                .prestige("Standart")
                .price(BigDecimal.valueOf(1231))
                .build();

        MockMultipartFile roomFile = new MockMultipartFile("file", "004.txt",
                "text/plain", "Test data".getBytes());
        List<MultipartFile> roomDataFiles = List.of(roomFile);
        List<Room> roomList = List.of(room);

        ResponseEntity<?> response = roomService.addedARoomToTheHotel(principal, "testStreet12", roomList, roomDataFiles);

        assertEquals(ResponseEntity.ok(roomList), response);
    }

    @Test
    void addedPhotosToRooms() {

    }

    @Test
    void modifyRoomName() {
    }

    @Test
    void forTestDeleteAll() {
    }
}