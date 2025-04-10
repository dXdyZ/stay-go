package com.lfey.statygo.repository;

import com.lfey.statygo.entity.Booking;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.entity.RoomType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {

    @Query("select r.room.id from Booking r where r.hotel.id = :hotelId and r.room.capacity = :capacity and " +
            "r.room.roomType = :roomType and r.startDate <= :rangeStart and r.endDate >= :rangeEnd")
    Set<Long> findReservationRangeIdBooking(@Param("hotelId") Long hotelId, @Param("capacity") Integer capacity,
                                            @Param("roomType") RoomType roomType, @Param("rangeStart") LocalDate startDate,
                                            @Param("rangeEnd") LocalDate endDate);

    @Query("select r.room.id from Booking r where r.hotel.id = :hotelId and r.room.capacity = :capacity and " +
            "r.startDate <= :rangeStart and r.endDate >= :rangeEnd")
    Set<Long> findReservationRangeIdSearching(@Param("hotelId") Long hotelId, @Param("capacity") Integer capacity,
                                              @Param("rangeStart") LocalDate startDate, @Param("rangeEnd") LocalDate endDate);

}
