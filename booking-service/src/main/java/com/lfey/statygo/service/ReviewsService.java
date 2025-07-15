package com.lfey.statygo.service;

import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.entity.Review;
import com.lfey.statygo.entity.redisEntity.HotelRatingDay;
import com.lfey.statygo.repository.jpaRepository.ReviewsRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final HotelRatingDayService hotelRatingDayService;

    public ReviewsService(ReviewsRepository reviewsRepository, HotelRatingDayService hotelRatingDayService) {
        this.reviewsRepository = reviewsRepository;
        this.hotelRatingDayService = hotelRatingDayService;
    }

    public void createReview(String username, String text, Integer grade, Hotel hotel) {
        reviewsRepository.save(Review.builder()
                        .username(username)
                        .reviewsDescription(text)
                        .grade(grade)
                        .hotel(hotel)
                .build());
        hotelRatingDayService.saveOrUpdate(hotel.getId(), grade, hotel.getGrade());
    }

    public HashMap<Long, Double> calculateAverageRatingIncludeDay() {
        List<HotelRatingDay> hotelRatingDays = hotelRatingDayService.getAllReviewsPerDay();
        HashMap<Long, Double> result = new HashMap<>();

        for (HotelRatingDay hotelRatingDay : hotelRatingDays) {
            result.put(hotelRatingDay.getHotelId(),
                    calculationAverageRating(hotelRatingDay.getAvgRating(), hotelRatingDay.getGrades()));
        }

        return result;
    }

    private double calculationAverageRating(Double avgRating, List<Integer> grades) {
        double dayResult = 0;

        for (Integer grade : grades) {
            dayResult += grade;
        }

        return (avgRating * 10 + dayResult) / (10 + grades.size());
    }
}
