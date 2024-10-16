package com.staygo.service.hotel_ser;

import com.staygo.enity.hotel.ArmoredRoom;
import com.staygo.repository.hotel_repo.ArmoredRoomRepository;
import com.staygo.repository.hotel_repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArmoredRoomService {
    private final ArmoredRoomRepository armoredRoomRepository;

    @Autowired
    public ArmoredRoomService(ArmoredRoomRepository armoredRoomRepository) {
        this.armoredRoomRepository = armoredRoomRepository;
    }
    
    public boolean findByArmoredData(String armoredData, String departureData) {
        List<ArmoredRoom> armoredRooms = armoredRoomRepository.findByDateArmoredAndDepartureDate(armoredData, departureData);
        return armoredData != null && !armoredData.isEmpty();
    }

    public ResponseEntity<?> armoredHotel() {

    }
}
