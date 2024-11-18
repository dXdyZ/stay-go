package com.staygo.service.car;

import com.staygo.enity.DTO.TransportDTO;
import com.staygo.enity.address.Address;
import com.staygo.enity.transport.Transport;
import com.staygo.enity.transport.TransportData;
import com.staygo.repository.transport_repo.TransportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CarServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CarServiceTest.class);

    @MockBean
    private TransportRepository transportRepository;

    @Autowired
    private CarService carService;

    @BeforeEach
    void init() {
        Transport transport = Transport.builder()
                .transportName("testCar")
                .id(1L)
                .address(Address.builder()
                        .city("Kaliningrad")
                        .numberHouse("33")
                        .country("Russian")
                        .zipCode("236002")
                        .street("Gorkogo")
                        .build())
                .build();
        when(transportRepository.findById(1L)).thenReturn(Optional.ofNullable(transport));
        when(transportRepository.findAllByTransportNameAndAddress_CountryAndAddress_City("testCar"
                ,"Russian", "Kaliningrad")).thenReturn(Collections.singletonList(transport));

        MockitoAnnotations.openMocks(this);
    }

    Transport getTransport() {
        return Transport.builder()
                .transportName("testCar")
                .id(1L)
                .address(Address.builder()
                        .city("Kaliningrad")
                        .numberHouse("33")
                        .country("Russian")
                        .zipCode("236002")
                        .street("Gorkogo")
                        .build())
                .build();
    }

    @Test
    void createCarValidData() {
        assertEquals(getTransportDTO(getTransport()), carService.createCar(getTransport(), getTransportData()));
    }

    @Test
    void createCarNoPhoto() {
        assertEquals(getTransportDTO(getTransport()), carService.createCar(getTransport(), null));
    }

    @Test
    void getCarById() {
        assertEquals(getTransport(), carService.getCarById(1L).get());
    }

    @Test
    void getCarByNameAndCountryAndCity() {
        assertEquals(Collections.singletonList(getTransport()), carService.getCarByNameAndCountryAndCity("testCar",
                "Russian", "Kaliningrad"));
    }


    TransportDTO getTransportDTO(Transport transport) {
        return carService.getTransportDTO(transport);
    }

    List<TransportData> getTransportData() {
        byte[] testData = {12, 12, 43, 1, 2, 5, 7, 12, 99, 12};
        return Collections.singletonList(TransportData.builder()
                .name("tets Data")
                .data(testData)
                .transport(getTransport())
                .createDate(new Date())
                .build());
    }
}