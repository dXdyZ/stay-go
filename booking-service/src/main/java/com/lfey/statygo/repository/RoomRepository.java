package com.lfey.statygo.repository;

import com.lfey.statygo.entity.Room;
import com.lfey.statygo.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r from Room r where r.hotel.id = :hotelId and r.capacity = :capacity and r.roomType = :roomType")
    List<Room> findRoomsByHotelIdAndRoomTypeAndCapacity(@Param("hotelId") Long hotelId, @Param("capacity") Integer capacity,
                                                        @Param("roomType") RoomType roomType);

    @Query("select r from Room r where r.hotel.id = :hotelId and r.capacity = :capacity")
    List<Room> findRoomsByHotelIdAndCapacity(@Param("hotelId") Long hotelId, @Param("capacity") Integer capacity);


    @Query("SELECT r.id FROM Room r " +
            "WHERE r.hotel.id = :hotelId " +
            "AND r.capacity = :capacity " +
            "AND (:roomType IS NULL OR r.roomType = :roomType) " +
            "AND r.id NOT IN (" +
            "   SELECT b.room.id FROM Booking b " +
            "   WHERE b.hotel.id = :hotelId " +
            "   AND b.startDate <= :rangeEnd " +
            "   AND b.endDate >= :rangeStart" +
            ")")
    List<Room> findAvailableRoomIds(
            @Param("hotelId") Long hotelId,
            @Param("capacity") Integer capacity,
            @Param("rangeStart") LocalDate startDate,
            @Param("rangeEnd") LocalDate endDate,
            @Param("roomType") RoomType roomType
    );

}
