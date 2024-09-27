package com.staygo.service;

import com.staygo.enity.Address;
import com.staygo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public boolean findAllParameterize(String country, String city, String street, String zipCode) {
        return addressRepository.findByCountryAndCityAndStreetAndZipCode(country, city, street, zipCode).isPresent();
    }

    public void save(Address address) {
        addressRepository.save(address);
    }

    public void deleteAll() {
        addressRepository.deleteAll();
    }
}
