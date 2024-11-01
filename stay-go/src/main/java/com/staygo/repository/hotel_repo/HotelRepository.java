package com.staygo.repository.hotel_repo;

import com.staygo.enity.hotel.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends CrudRepository<Hotel, Long> {
    Optional<Hotel> findByName(String name);
    Optional<Hotel> findByAddress_CountryAndAddress_CityAndAddress_Street(String country, String city, String street);
    Optional<Hotel> findByUsers_UsernameAndAddress_Street(String username, String street);
    List<Hotel> findAllByUsers_Username(String username, Pageable pageable);
    Hotel findByAddress_CityAndName(String city, String name);
    List<Hotel> findAllByOrderByGradeDesc(Pageable pageable);
    List<Hotel> findAllByAddress_CityAndAddress_Country(String city, String country,Pageable pageable);
    List<Hotel> findAllByGradeAndAddress_CityAndAddress_Country(Integer grade, String city, String county ,Pageable pageable);
    List<Hotel> findAllByAddress_CountryOrderByGradeDesc(String countryName, Pageable pageable);
    List<Hotel> findAllByAddress_CityOrderByGradeDesc(String cityName, Pageable pageable);
    List<Hotel> findAllByAddress_CountryAndAddress_CityOrderByGradeDesc(String countryName, String cityName, Pageable pageable);
    List<Hotel> findAllByGradeOrderByGradeDesc(Integer gradeNumber, Pageable pageable);
}
