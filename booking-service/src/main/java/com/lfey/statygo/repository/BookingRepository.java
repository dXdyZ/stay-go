package com.lfey.statygo.repository;

import com.lfey.statygo.entity.Booking;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.entity.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"hotel", "room"})
    @Query("SELECT b FROM Booking b " +
           "WHERE b.hotel.id = :hotelId " +
           "AND b.startDate <= :rangeEnd " +
           "AND b.endDate >= :rangeStart")
    Page<Booking> findAllBookingByPeriodAtHotel(
        @Param("hotelId") Long hotelId,
        @Param("rangeStart") LocalDate rangeStart,
        @Param("rangeEnd") LocalDate rangeEnd,
        Pageable pageable
    );

    @EntityGraph(attributePaths = {"hotel", "room"}) //Решает проблему 1+N запросов
    @Query("select b from Booking b where b.hotel.id = :hotelId")
    Page<Booking> findAllByHotel_Id(@Param("hotelId") Long hotelId, Pageable pageable);

    @EntityGraph(attributePaths = {"hotel", "room"})
    Page<Booking> findAllByUsername(@Param("username") String username, Pageable pageable);
}
