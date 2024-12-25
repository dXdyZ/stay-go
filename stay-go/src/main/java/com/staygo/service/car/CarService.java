package com.staygo.service.car;

import com.staygo.custom_exception.DateException;
import com.staygo.enity.DTO.TransportDTO;
import com.staygo.enity.transport.ArmoredTransport;
import com.staygo.enity.transport.Transport;
import com.staygo.enity.transport.TransportData;
import com.staygo.repository.transport_repo.TransportRepository;
import com.staygo.service.DateCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final TransportRepository transportRepository;
    private final DateCheck dateCheck;

    @Autowired
    public CarService(TransportRepository transportRepository, DateCheck dateCheck) {
        this.transportRepository = transportRepository;
        this.dateCheck = dateCheck;
    }

    @Transactional
    public Object createCar(Transport transport, List<TransportData> transportData) {
        if (transport.getTransportName().isBlank()) {
            return "Transport name shouldn't empty";
        }
        if (transport.getAddress() == null) {
            return "Address shouldn`t empty";
        } else if (transport.getAddress().getStreet().isBlank()) {
            return "Address street shouldn`t empty";
        } else if (transport.getAddress().getCountry().isBlank()) {
            return "Address country shouldn`t empty";
        } else if (transport.getAddress().getCity().isBlank()) {
            return "Address city shouldn`t empty";
        } else if (transport.getAddress().getNumberHouse().isBlank()) {
            return "Address number house shouldn`t empty";
        }
        if ((transportData != null)) {
            for (TransportData transportDatum : transportData) {
                transportDatum.setTransport(transport);
            }
            transport.setTransportData(transportData);
            transportRepository.save(transport);
            return getTransportDTO(transport);
        } else {
            transportRepository.save(transport);
            return getTransportDTO(transport);
        }
    }

    @Transactional
    public Optional<Transport> getCarById(Long id) {
        return transportRepository.findById(id);
    }

    @Transactional
    @Cacheable(value = "findCarByNameAndCityAndCountry", key = "#name + '-' + #country + '-' + #city")
    public List<Transport> getCarByNameAndCountryAndCity(String name, String country, String city) {
        return transportRepository.findAllByTransportNameAndAddress_CountryAndAddress_City(name, country, city);
    }

    public TransportDTO getTransportDTO(Transport transport) {
        return TransportDTO.builder()
                .name(transport.getTransportName())
                .street(transport.getAddress().getStreet())
                .city(transport.getAddress().getCity())
                .numberHouse(transport.getAddress().getCity())
                .country(transport.getAddress().getCountry())
                .price(String.valueOf(transport.getPrice()))
                .build();
    }


    @Transactional
    public TransportDTO getUserDataTransport(String carName, String country, String city, String reservationDate, String dueDate) {
        return getTransportDTO(getNotReservationTransport(carName, country, city, reservationDate, dueDate));
    }

    @Transactional
    public Transport getNotReservationTransport(String carName, String country, String city, String reservationDate, String dueDate) {
        try {
            dateCheck.checkForThePresent(dueDate, reservationDate);
            List<Transport> transportList = getCarByNameAndCountryAndCity(carName, country, city);
            Transport transportForReservation = null;
            for (Transport transport : transportList) {
                boolean isAvailable = true;
                for (ArmoredTransport armoredTransport : transport.getArmoredTransport()) {
                    if (armoredTransport.getArmoredDate().equals(reservationDate) || armoredTransport.getEndDateArmored().equals(dueDate)) {
                        isAvailable = false;
                        break;
                    }
                }
                if (isAvailable) {
                    transportForReservation = transport;
                }
            }
            return transportForReservation;
        } catch (DateException e) {
            throw new RuntimeException(e);
        }
    }
}
