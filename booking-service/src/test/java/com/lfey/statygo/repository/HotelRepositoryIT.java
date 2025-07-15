package com.lfey.statygo.repository;

import com.lfey.statygo.entity.*;
import com.lfey.statygo.repository.jpaRepository.HotelRepository;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HotelRepositoryIT {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("booking-test-db")
            .withUsername("booking")
            .withPassword("booking");

    @DynamicPropertySource
    public static void configurer(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> true);
    }

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private static Hotel hotel;
    private static Address address;
    private static Room room;
    private Statistics statistics;

    @BeforeEach
    void clearHibernateStatics() {
        statistics = entityManagerFactory.unwrap(SessionFactoryImpl.class).getStatistics();
        statistics.clear();
    }

    @BeforeAll
    static void initData() {
        address = Address.builder()
                        .country("Russian")
                        .city("Moscow")
                        .street("moscow")
                        .houseNumber("23 a")
                        .postalCode(123123L)
                        .build();

        hotel = Hotel.builder()
                .name("test hotel")
                .address(address)
                .stars(4)
                .grade(3.2)
                .description("Hello i`m new hotel. Me created for testing this method")
                .build();

        room = Room.builder()
                .number(12)
                .description("Hello i`m new room. Me created for testing this method")
                .hotel(hotel)
                .roomType(RoomType.BUSINESS)
                .capacity(1)
                .bedType(BedType.SINGLE)
                .pricePerDay(123.1)
                .autoApprove(true)
                .isActive(true)
                .build();
        hotel.getRoom().add(room);
    }

    @Test
    @Transactional
    void saveHotel_ValidData() {
        Hotel result = hotelRepository.save(hotel);

        assertNotNull(result);
        assertNotNull(result.getAddress());
        assertNotNull(result.getRoom().get(0));
        assertEquals(address, result.getAddress());
        assertEquals("test hotel", result.getName());
        assertEquals("moscow", result.getAddress().getStreet());
    }

    @Test
    void findById_WhenExistsHotel() {
        Hotel result = hotelRepository.findById(1L).orElse(null);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Hotel", result.getName());
        assertEquals("Russia", result.getAddress().getCountry());
        assertNotNull(result.getRoom().get(0));
        assertEquals(101, result.getRoom().get(0).getNumber());
        assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void findAll_WhenExistsHotels() {
        List<Hotel> resul = hotelRepository.findAll();

        assertNotNull(resul.get(0));
        assertEquals(1, resul.size());
    }
}