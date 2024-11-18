package com.staygo.service.car;

import com.staygo.castom_exe.DateException;
import com.staygo.enity.DTO.rabbit.ArmoredRoomDTO;
import com.staygo.enity.DTO.rabbit.CarReservationDTO;
import com.staygo.enity.transport.ArmoredTransport;
import com.staygo.enity.transport.Transport;
import com.staygo.repository.transport_repo.ArmoredTransportRepository;
import com.staygo.service.DateCheck;
import com.staygo.service.PayService;
import com.staygo.service.rabbit.RabbitMessage;
import com.staygo.service.user_ser.UserService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class CarReservationService {
    private final CarService carService;
    private final ArmoredTransportRepository armoredTransportRepository;
    private final UserService userService;
    private final RabbitMessage rabbitMessage;
    private final DateCheck dateCheck;
    private final PayService payService;


    @Autowired
    public CarReservationService(CarService carService, ArmoredTransportRepository armoredTransportRepository, UserService userService, RabbitMessage rabbitMessage, DateCheck dateCheck, PayService payService) {
        this.carService = carService;
        this.armoredTransportRepository = armoredTransportRepository;
        this.userService = userService;
        this.rabbitMessage = rabbitMessage;
        this.dateCheck = dateCheck;
        this.payService = payService;
    }

    @Transactional
    public void reservationCar(String city, String country, String carName, Principal principal, String reservationDate, String dueDate) {
        ArmoredTransport armoredTransport = ArmoredTransport.builder()
                .armoredDate(reservationDate)
                .endDateArmored(dueDate)
                .users(userService.findByPrincipal(principal)
                        .get())
                .transport(carService.getNoReservationTransport(carName, country, city, reservationDate, dueDate))
                .build();
        armoredTransportRepository.save(armoredTransport);

        rabbitMessage.sendDataReservationCar(getCarReservationDTO(armoredTransport));
    }

    public CarReservationDTO getCarReservationDTO(ArmoredTransport armoredTransport){
        return CarReservationDTO.builder()
                .careteDate(new Date())
                .username(armoredTransport.getUsers().getUsername())
                .email(armoredTransport.getUsers().getEmail())
                .price(String.valueOf(payService.costCalculation(armoredTransport.getArmoredDate(), armoredTransport.getEndDateArmored(), armoredTransport.getTransport().getPrice())))
                .build();
    }


    @Transactional
    public List<ArmoredTransport> getAllTransportByCityAndCountryAndTransportName(String city, String country, String transportName) {
        return armoredTransportRepository.findAllTransport_Address_CityAndTransport_Address_CountryAndTransport_TransportName(city, country, transportName);
    }

    @Transactional
    public List<ArmoredTransport> getAllReservationTransport() {
        return Lists.newArrayList(armoredTransportRepository.findAll());
    }
}
