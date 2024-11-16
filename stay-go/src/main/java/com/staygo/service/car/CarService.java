package com.staygo.service.car;

import com.staygo.enity.DTO.TransportDTO;
import com.staygo.enity.transport.Transport;
import com.staygo.enity.transport.TransportData;
import com.staygo.repository.transport_repo.TransportDataRepository;
import com.staygo.repository.transport_repo.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final TransportRepository transportRepository;

    @Autowired
    public CarService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
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
        if ((transportData != null) || !transportData.isEmpty()) {
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
                .build();
    }
}
