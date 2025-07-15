package com.lfey.statygo.repository.jpaRepository;

import com.lfey.statygo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, Long> {
}
