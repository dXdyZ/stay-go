package com.staygo.service.hotel_ser;

import com.staygo.enity.hotel.ArmoredRoom;
import com.staygo.repository.hotel_repo.ArmoredRoomRepository;
import com.staygo.repository.hotel_repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (armoredData != null && !armoredData.isEmpty()) return true; else return false;
    }
}
