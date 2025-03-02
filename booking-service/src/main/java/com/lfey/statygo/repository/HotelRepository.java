package com.lfey.statygo.repository;

import com.lfey.statygo.entity.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends CrudRepository<Hotel, Long>,
        JpaSpecificationExecutor<Hotel> {

    List<Hotel> findByName(String name, Pageable pageable);

    Long id(Long id);
}
