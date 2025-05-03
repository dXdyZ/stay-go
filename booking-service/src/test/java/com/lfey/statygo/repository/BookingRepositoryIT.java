package com.lfey.statygo.repository;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.CustomPageable;
import com.lfey.statygo.entity.Booking;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryIT {

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
    private BookingRepository bookingRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Statistics statistics;

    @BeforeEach
    void cleanHibernateStatistic() {
        statistics = entityManagerFactory.unwrap(SessionFactoryImpl.class).getStatistics();
        statistics.clear();
    }

    @Test
    public void findAllByHotel_Id_WhenBookingExists() {
        Page<Booking> result = bookingRepository.findAllByHotel_Id(
                1L,
                CustomPageable.getPageable(0, 5) // page=0, size=5
        );

        assertFalse(result.isEmpty(), "Результат не должен быть пустым");
        assertEquals(1, result.getContent()
                .stream()
                .map(Booking::getHotel)
                .distinct()
                .count()
        );
    }


    @Test
    void findAllBookingByPeriodAtHotel_WhenBookingAtDateExists() {
        Page<Booking> result = bookingRepository.findAllBookingByPeriodAtHotel(
                1L,
                CustomDateFormatter.localDateFormatter("2025-06-05"),
                CustomDateFormatter.localDateFormatter("2025-06-30"),
                CustomPageable.getPageable(0, 5)
        );


        assertFalse(result.isEmpty(), "Результат не должен быть пустым");
        assertEquals(2, result.getContent().stream()
                .map(Booking::getRoom)
                .distinct()
                .count());
    }
}













