package com.staygo.repository.address_repo;

import com.staygo.enity.address.AddressForAirport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportAddressRepository extends CrudRepository<AddressForAirport, Long> {
}
