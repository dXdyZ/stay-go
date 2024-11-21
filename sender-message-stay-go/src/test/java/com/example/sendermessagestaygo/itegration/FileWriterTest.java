package com.example.sendermessagestaygo.itegration;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import com.example.sendermessagestaygo.enity.CarReservationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class FileWriterTest {

    private final FileWriterGateway fileWriterGateway;

    @Autowired
    public FileWriterTest(FileWriterGateway fileWriterGateway) {
        this.fileWriterGateway = fileWriterGateway;
    }

    @Test
    void testWriteFile() {
        ArmoredRoomDTO armoredRoomDTO = ArmoredRoomDTO.builder()
                .city("Test")
                .country("Test")
                .email("test@email.test")
                .id(12L)
                .createDate(new Date())
                .price("12331")
                .dateArmored("23.23.231")
                .departureDate("12.12.12")
                .hotelName("test")
                .houseNumber("231")
                .prestige("test")
                .roomNumber("123")
                .username("test")
                .build();
        CarReservationDTO carReservationDTO = new CarReservationDTO(12L, "test",
                "2302322.12", "123123", new Date(), "123", "qwe", "123", "1231", "123", "123",
                "12");
        fileWriterGateway.writeToFile("log-reservation-room.json", armoredRoomDTO);
        fileWriterGateway.writeToFile("log-reservation-car.json", carReservationDTO);
    }
}