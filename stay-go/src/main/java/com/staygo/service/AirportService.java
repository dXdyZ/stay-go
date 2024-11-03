package com.staygo.service;

import com.staygo.enity.address.AddressForAirport;
import com.staygo.repository.address_repo.AirportAddressRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirportService {
    private final AirportAddressRepository airportAddressRepository;

    @Autowired
    public AirportService(AirportAddressRepository airportAddressRepository) {
        this.airportAddressRepository = airportAddressRepository;
    }

    public void addAddress(@NotNull AddressForAirport address) {
        airportAddressRepository.save(address);
    }

    public AddressForAirport getAddressAirportById(Long id) {
        return airportAddressRepository.findById(id).orElseThrow(() -> {
            new NullPointerException("Такого аэропорта нету");
            return null;
        });
    }

    public AddressForAirport getAddressAirportByCityAndCounty(String city, String county) {
        return airportAddressRepository.findByCityAndCountry(city, county).orElseThrow(() -> {
            new NullPointerException("Аэропорта по такому адресу нету или его вовсе не существует");
            return null;
        });
    }
}
