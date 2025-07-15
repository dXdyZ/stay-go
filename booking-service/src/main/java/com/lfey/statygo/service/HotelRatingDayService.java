package com.lfey.statygo.service;

import com.lfey.statygo.entity.redisEntity.HotelRatingDay;
import com.lfey.statygo.repository.redisRepository.HotelRatingDayRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class HotelRatingDayService {
    private final HotelRatingDayRepository hotelRatingDayRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public HotelRatingDayService(HotelRatingDayRepository hotelRatingDayRepository,
                                 RedisTemplate<String, Object> redisTemplate) {
        this.hotelRatingDayRepository = hotelRatingDayRepository;
        this.redisTemplate = redisTemplate;
    }

    public void saveOrUpdate(Long hotelId, Integer grade, Double avgRating) {
        Optional<HotelRatingDay> existingRatingOpt = hotelRatingDayRepository.findById(hotelId);

        boolean isNewEntity = existingRatingOpt.isEmpty();

        HotelRatingDay hotelRatingDay = existingRatingOpt.orElse(new HotelRatingDay(hotelId, avgRating));

        hotelRatingDay.addGrade(grade);
        hotelRatingDayRepository.save(hotelRatingDay);

        if (isNewEntity) redisTemplate.expire("HotelRatingDay:" + hotelId, 24, TimeUnit.HOURS);

    }

    public List<HotelRatingDay> getAllReviewsPerDay() {
        Iterable<HotelRatingDay> iterable = hotelRatingDayRepository.findAll();
        List<HotelRatingDay> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result;
    }
}
