package com.lfey.statygo.repository;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.entity.Room;
import com.lfey.statygo.entity.RoomType;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.stat.Statistics;
import org.hibernate.stat.internal.StatisticsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomRepositoryIT {

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
    private RoomRepository roomRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Statistics statistics;

    @BeforeEach
    void cleanHibernateStatistic() {
        statistics = entityManagerFactory.unwrap(SessionFactoryImpl.class).getStatistics();
        statistics.clear();
    }

    @Test
    void findAvailableRoomIds_WhenDataCrossingOver_ThenReturn0Rooms() {
        var hotelId = 1L;
        var capacity = 2;
        var startDate = CustomDateFormatter.localDateFormatter("2025-06-01");
        var endDate = CustomDateFormatter.localDateFormatter("2025-06-05");
        var roomType = RoomType.STANDARD;


        List<Room> result = roomRepository.findAvailableRoomIds(hotelId, capacity, startDate, endDate, roomType);

        assertEquals(0, result.size());
    }

    @Test
    void findAvailableRoomIds_WhenDataDontCrossingOver_ThenReturn1Rooms() {
        var hotelId = 1L;
        var capacity = 2;
        var startDate = CustomDateFormatter.localDateFormatter("2025-06-21");
        var endDate = CustomDateFormatter.localDateFormatter("2025-06-25");
        var roomType = RoomType.STANDARD;


        List<Room> result = roomRepository.findAvailableRoomIds(hotelId, capacity, startDate, endDate, roomType);

        assertEquals(1, statistics.getPrepareStatementCount());
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
    }
}