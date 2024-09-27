package com.staygo.repository.hotel_repo;

import com.staygo.enity.hotel.ArmoredHotel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArmoredHotelRepository extends CrudRepository<ArmoredHotel, Long> {
}
