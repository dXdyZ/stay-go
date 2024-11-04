package com.example.sendermessagestaygo.service;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendBookingMail {
    private final MailService mailService;

    @Autowired
    public SendBookingMail(MailService mailService) {
        this.mailService = mailService;
    }

    public String sendMail(ArmoredRoomDTO armoredRoomDTO) {
        mailService.sendMail(armoredRoomDTO.getEmail(), "Booking data", mapArmoredRoomDTOToString(armoredRoomDTO));
        return armoredRoomDTO.toString();
    }

    public String mapArmoredRoomDTOToString(ArmoredRoomDTO armoredRoomDTO) {
        return "Ваша бронь:\n" +
                "ваше имя: " + armoredRoomDTO.getUsername() + "\n" +
                "ваш email: " + armoredRoomDTO.getEmail() + "\n" +
                "дата бронирования: " + armoredRoomDTO.getCreateDate() + "\n" +
                "номер комнаты: " + armoredRoomDTO.getRoomNumber() + "\n" +
                "класс комнаты: " + armoredRoomDTO.getPrestige() + "\n" +
                "цена на время aренды: " + armoredRoomDTO.getPrice() + "\n" +
                "дата заезда: " + armoredRoomDTO.getDateArmored() + "\n" +
                "дата отъезда: " + armoredRoomDTO.getDepartureDate() + "\n" +
                "Название отеля: " + armoredRoomDTO.getHotelName() + "\n" +
                "Адрес отеля: " + armoredRoomDTO.getStreet() + ", " +
                armoredRoomDTO.getHouseNumber() + ", " + armoredRoomDTO.getCity() + ", " +
                armoredRoomDTO.getCountry();

    }
}
