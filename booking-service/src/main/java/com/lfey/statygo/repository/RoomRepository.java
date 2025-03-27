package com.lfey.statygo.repository;

import com.lfey.statygo.entity.Room;
import com.lfey.statygo.entity.RoomType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    @Query("select r from Room r where r.hotel.id = :hotelId and r.capacity = :capacity and r.roomType = :roomType")
    List<Room> findRoomsByHotelIdAndRoomTypeAndCapacity(@Param("hotelId") Long hotelId, @Param("capacity") Integer capacity,
                                                        @Param("roomType") RoomType roomType);
}
