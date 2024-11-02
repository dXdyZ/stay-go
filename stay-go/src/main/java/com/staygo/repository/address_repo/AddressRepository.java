package com.staygo.repository.address_repo;

import com.staygo.enity.address.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
    Optional<Address> findByStreetAndNumberHouseAndZipCode(String street,
                                                           String houseNumber,
                                                           String zipCode);
}
