package com.staygo.repository.hotel_repo;

import com.staygo.enity.hotel.Comments;
import com.staygo.enity.hotel.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends CrudRepository<Comments, Long> {
    List<Comments> findAllByHotel(Hotel hotel, Pageable pageable);
}
