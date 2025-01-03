package com.staygo.repository.address_repo;

import com.staygo.enity.address.AddressForAirport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportAddressRepository extends CrudRepository<AddressForAirport, Long> {
    Optional<AddressForAirport> findByCityAndCountry(String city, String country);
}
