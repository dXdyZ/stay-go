package com.staygo.repository.hotel_repo;

import com.staygo.enity.hotel.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    Optional<Room> findByRoomName(String roomName);
}
