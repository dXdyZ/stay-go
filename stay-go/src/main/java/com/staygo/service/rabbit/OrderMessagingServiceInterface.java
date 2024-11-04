package com.staygo.service.rabbit;

import com.staygo.enity.DTO.rabbit.ArmoredRoomDTO;
import com.staygo.enity.hotel.ArmoredRoom;

public interface OrderMessagingServiceInterface {
    void sendDataBooking(ArmoredRoomDTO armoredRoomDTO);
}
