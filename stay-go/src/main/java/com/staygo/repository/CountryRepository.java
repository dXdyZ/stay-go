package com.staygo.repository;


import com.staygo.enity.weather.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    Boolean existsByLatitudeAndLongitude(Double latitude, Double longitude);

    Optional<Country> findByName(String name);
}
