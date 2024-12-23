package com.staygo.service.car;

import com.staygo.enity.DTO.rabbit.CarReservationDTO;
import com.staygo.enity.transport.ArmoredTransport;
import com.staygo.repository.transport_repo.ArmoredTransportRepository;
import com.staygo.service.PayService;
import com.staygo.service.rabbit.RabbitMessage;
import com.staygo.service.user_ser.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;

@Service
public class CarReservationService {
    private final CarService carService;
    private final ArmoredTransportRepository armoredTransportRepository;
    private final UserService userService;
    private final RabbitMessage rabbitMessage;;
    private final PayService payService;


    @Autowired
    public CarReservationService(CarService carService, ArmoredTransportRepository armoredTransportRepository,
                                 UserService userService, RabbitMessage rabbitMessage, PayService payService) {
        this.carService = carService;
        this.armoredTransportRepository = armoredTransportRepository;
        this.userService = userService;
        this.rabbitMessage = rabbitMessage;
        this.payService = payService;
    }

    @Transactional
    public void reservationCar(String city, String country, String carName, Principal principal, String reservationDate, String dueDate) {
        ArmoredTransport armoredTransport = ArmoredTransport.builder()
                .armoredDate(reservationDate)
                .endDateArmored(dueDate)
                .users(userService.findByPrincipal(principal)
                        .get())
                .transport(carService.getNotReservationTransport(carName, country, city, reservationDate, dueDate))
                .build();
        ArmoredTransport savedReservation = armoredTransportRepository.save(armoredTransport);

        rabbitMessage.sendDataReservationCar(getCarReservationDTO(savedReservation));
    }

    public CarReservationDTO getCarReservationDTO(ArmoredTransport armoredTransport){
        return CarReservationDTO.builder()
                .id(armoredTransport.getId())
                .careteDate(new Date())
                .username(armoredTransport.getUsers().getUsername())
                .email(armoredTransport.getUsers().getEmail())
                .price(String.valueOf(payService.costCalculation(armoredTransport.getArmoredDate(),
                        armoredTransport.getEndDateArmored(), armoredTransport.getTransport().getPrice())))
                .build();
    }
}
