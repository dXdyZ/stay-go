package com.lfey.statygo.repository.redisRepository;

import com.lfey.statygo.entity.redisEntity.HotelRatingDay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRatingDayRepository extends CrudRepository<HotelRatingDay, Long> {
}
