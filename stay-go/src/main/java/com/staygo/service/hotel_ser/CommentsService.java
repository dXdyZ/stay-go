package com.staygo.service.hotel_ser;

import com.staygo.enity.hotel.Comments;
import com.staygo.enity.hotel.Hotel;
import com.staygo.repository.hotel_repo.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {
    private final CommentsRepository commentsRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public List<Comments> getCommentsByHotel(Hotel hotel) {
        Pageable pageable = PageRequest.of(0, 10);
        return commentsRepository.findAllByHotel(hotel, pageable);
    }
}
