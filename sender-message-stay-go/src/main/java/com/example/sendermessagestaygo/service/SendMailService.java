package com.example.sendermessagestaygo.service;

import com.example.sendermessagestaygo.enity.ArmoredRoomDTO;
import com.example.sendermessagestaygo.enity.CarReservationDTO;
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

    public void sendMailForBooking(ArmoredRoomDTO armoredRoomDTO) {
        mailService.sendMail(armoredRoomDTO.getEmail(), "Booking data", mapArmoredRoomDTOToString(armoredRoomDTO));
    }

    public void sendMailCodeForUserAuth(UserRegCodeDTO userRegCodeDTO) {
        mailService.sendMail(userRegCodeDTO.getEmail(), "Verification email", userRegCodeDTO.getCode().toString());
    }

    public void sendMailCarReservation(CarReservationDTO carReservationDTO) {
        mailService.sendMail(carReservationDTO.getEmail(), "Car reservation data", mapCarReservationDTOForSendMail(carReservationDTO));
    }

    private String mapCarReservationDTOForSendMail(CarReservationDTO carReservationDTO) {
        return "------ Подтверждение бронирования автомобиля ------\n" +
                "- код бронирования: " + carReservationDTO.getId() + "\n" +
                "- ваше имя: " + carReservationDTO.getUsername() + "\n" +
                "- ваш email: " + carReservationDTO.getEmail() + "\n" +
                "- дата бронирования: " + carReservationDTO.getCareteDate() + "\n" +
                "- имя автомобиля: " + carReservationDTO.getCarName() + "\n" +
                "- дата резервации: " + carReservationDTO.getReservationDate() + "\n" +
                "- дата сдачи: " + carReservationDTO.getDueDate() + "\n" +
                "- общая ссума на срок бронирования: " + carReservationDTO.getPrice() + "\n" +
                "- адрес: " + carReservationDTO.getStreet() + ", " + carReservationDTO.getHouseNumber() +
                ", " + carReservationDTO.getCity() + ", " + carReservationDTO.getCountry();
    }

    private String mapArmoredRoomDTOToString(ArmoredRoomDTO armoredRoomDTO) {
        return "------ Подтверждение бронирования ------\n" +
                "- код бронирования: " + armoredRoomDTO.getId() + "\n" +
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
