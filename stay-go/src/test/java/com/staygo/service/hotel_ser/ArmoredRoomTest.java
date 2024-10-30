package com.staygo.service.hotel_ser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.enity.user.Role;
import com.staygo.enity.user.Users;
import com.staygo.repository.user_repo.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.util.Date;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArmoredRoomTest {

    @Autowired
    private ArmoredRoomService armoredRoomService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserRepository userRepository;

    private Principal principal;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void init() {
        principal = () -> "testUser";
    }

    @BeforeEach
    void createUserForTest() {
        Users users = Users.builder()
                .username("testUser")
                .email("test@gmail.com")
                .createDate(new Date())
                .password("werpipl15")
                .phoneNumber("8999999999")
                .role(Role.ROLE_ADMIN)
                .build();
//        userRepository.save(users);
    }


//    @Test
//    @Rollback(false)
//    void createDataForTestMethodInArmoredClass() throws JsonProcessingException {
//        Room room = Room.builder()
//                .roomName("001")
//                .roomStatus("free")
//                .prestige("president")
//                .price(BigDecimal.valueOf(12300))
//                .build();
//
//        Hotel hotel = Hotel.builder()
//                .name("Test hotel for the method")
//                .grade(4)
//                .address(Address.builder()
//                        .city("kaliningrad")
//                        .numberHouse("45")
//                        .country("Russian")
//                        .street("Gorkogo")
//                        .zipCode("236006")
//                        .build())
//                .rooms(Arrays.asList(room)).build();
//
//        String hotelJson = objectMapper.writeValueAsString(hotel);
//
//        ResponseEntity<?> response = hotelService.createdHotel(hotelJson, null, principal);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }


}
