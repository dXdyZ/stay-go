package com.staygo.repository.hotel_repo;

import com.staygo.enity.hotel.ArmoredRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArmoredRoomRepository extends CrudRepository<ArmoredRoom, Long> {
    List<ArmoredRoom> findByDateArmoredAndDepartureDate(String armoredDate, String departureData);
}
