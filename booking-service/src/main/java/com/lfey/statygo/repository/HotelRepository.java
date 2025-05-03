package com.lfey.statygo.repository;

import com.lfey.statygo.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @EntityGraph(attributePaths = {"address", "room"})
    Optional<Hotel> findById(@NonNull Long id);


    @EntityGraph(attributePaths = {"address", "room"})
    Page<Hotel> findAll(Specification<Hotel> specification,@NonNull Pageable pageable);
}
