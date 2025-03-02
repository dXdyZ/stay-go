package com.lfey.statygo.repository;

import com.lfey.statygo.entity.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {

}
