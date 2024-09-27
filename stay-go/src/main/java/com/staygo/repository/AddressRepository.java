package com.staygo.repository;

import com.staygo.enity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
    Optional<Address> findByCountryAndCityAndStreetAndZipCode(String country,
                                                              String city,
                                                              String street,
                                                              String zipCode);
}
