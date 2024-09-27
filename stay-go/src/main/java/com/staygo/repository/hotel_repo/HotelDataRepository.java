package com.staygo.repository.hotel_repo;

import com.staygo.enity.hotel.HotelData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HotelDataRepository extends CrudRepository<HotelData, Long> {
}
