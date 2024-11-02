package com.staygo.service;

import com.staygo.enity.address.Address;
import com.staygo.repository.address_repo.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public boolean findAllParameterize(String houseNumber, String street, String zipCode) {
        return addressRepository.findByStreetAndNumberHouseAndZipCode(street, houseNumber, zipCode).isPresent();
    }

    public void save(Address address) {
        addressRepository.save(address);
    }

    public void deleteAll() {
        addressRepository.deleteAll();
    }
}
