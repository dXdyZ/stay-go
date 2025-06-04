package com.lfey.statygo.repository;

import com.lfey.statygo.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @EntityGraph(attributePaths = {"address", "room", "photos"})
    Optional<Hotel> findById(@NonNull Long id);

    @Query("select h.id from Hotel h " +
            "where (:stars is null or h.stars = :stars) " +
            "and (:country is null or h.address.country = :country) " +
        "and (:city is null or h.address.city = :city)")
    Page<Long> findFilteredHotelIds(@Param("stars") Integer stars, @Param("country") String country,
                                    @Param("city") String city, @NonNull Pageable pageable);

    @Query("select h from Hotel h " +
            "left join fetch h.room " +
            "left join fetch h.address " +
            "left join fetch h.photos " +
            "where h.id in :ids")
    List<Hotel> findHotelsWithDetailsByIds(@Param("ids") List<Long> ids);
}
