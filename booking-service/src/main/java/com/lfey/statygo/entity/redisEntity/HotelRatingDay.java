package com.lfey.statygo.entity.redisEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "HotelRatingDay")
public class HotelRatingDay implements Serializable {
    @Id
    private Long hotelId;

    private Double avgRating;

    @Builder.Default
    private List<Integer> grades = new ArrayList<>();

    public HotelRatingDay(Long hotelId, Integer grade) {
        this.hotelId = hotelId;
        grades.add(grade);
    }

    public HotelRatingDay(Long hotelId) {
        this.hotelId = hotelId;
    }

    public HotelRatingDay(Long hotelId, Double avgRating) {
        this.hotelId = hotelId;
        this.avgRating = avgRating;
    }

    public HotelRatingDay addGrade(Integer grade) {
        this.grades.add(grade);
        return this;
    }
}
