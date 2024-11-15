package com.example.sendermessagestaygo.service;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import com.example.sendermessagestaygo.enity.UserRegCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {
    private final MailService mailService;

    @Autowired
    public SendMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public String sendMailForBooking(ArmoredRoomDTO armoredRoomDTO) {
        mailService.sendMail(armoredRoomDTO.getEmail(), "Booking data", mapArmoredRoomDTOToString(armoredRoomDTO));
        return armoredRoomDTO.toString();
    }

    public String sendMailCodeForUserAuth(UserRegCodeDTO userRegCodeDTO) {
        mailService.sendMail(userRegCodeDTO.getEmail(), "Verification email", userRegCodeDTO.getCode().toString());
        return userRegCodeDTO.getCode() + "----" + userRegCodeDTO.getEmail();
    }

    public String mapArmoredRoomDTOToString(ArmoredRoomDTO armoredRoomDTO) {
        return "------ Подтверждение бронирования ------\n" +
                "- ваше имя: " + armoredRoomDTO.getUsername() + "\n" +
                "- ваш email: " + armoredRoomDTO.getEmail() + "\n" +
                "- дата бронирования: " + armoredRoomDTO.getCreateDate() + "\n" +
                "- номер комнаты: " + armoredRoomDTO.getRoomNumber() + "\n" +
                "- класс комнаты: " + armoredRoomDTO.getPrestige() + "\n" +
                "- цена на время aренды: " + armoredRoomDTO.getPrice() + "\n" +
                "- дата заезда: " + armoredRoomDTO.getDateArmored() + "\n" +
                "- дата отъезда: " + armoredRoomDTO.getDepartureDate() + "\n" +
                "- Название отеля: " + armoredRoomDTO.getHotelName() + "\n" +
                "- Адрес отеля: " + armoredRoomDTO.getStreet() + ", " +
                armoredRoomDTO.getHouseNumber() + ", " + armoredRoomDTO.getCity() + ", " +
                armoredRoomDTO.getCountry();

    }
}
