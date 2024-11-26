package com.staygo.repository.hotel_repo;

import com.staygo.enity.hotel.Hotel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends CrudRepository<Hotel, Long> {
    Optional<Hotel> findByName(String name);
    Optional<Hotel> findByAddress_CountryAndAddress_CityAndAddress_Street(String country, String city, String street);

    Optional<Hotel> findByNameAndAddress_CityAndAddress_CountryAndAddress_StreetAndUsers_Username(@NotNull @Size(
            min = 5,
            max = 255,
            message = "Имя отеля должно быть в адекватных приделах"
    ) String name, @NotNull @Size(
            min = 2,
            max = 255,
            message = "Требуетяс название города, максимум 255 символов"
    ) String addressCity, @NotNull @Size(
            min = 2,
            max = 200,
            message = "Требуется название страны, максимум 200 символов"
    ) String addressCountry, @NotNull @Size(
            min = 4,
            max = 255,
            message = "Требуется улица, максимум 255 символов"
    ) String addressStreet, @NotNull @Size(
            min = 2,
            max = 40,
            message = "Требуется имя, максимум 40 символо"
    ) String usersUsername);

    List<Hotel> findAllByUsers_Username(String username, Pageable pageable);
    Hotel findByAddress_CityAndNameAndAddress_Street(String city, String name, String street);
    List<Hotel> findAllByOrderByGradeDesc(Pageable pageable);
    List<Hotel> findAllByAddress_CityAndAddress_Country(String city, String country,Pageable pageable);
    List<Hotel> findAllByGradeAndAddress_CityAndAddress_Country(Integer grade, String city, String county ,Pageable pageable);
    List<Hotel> findAllByAddress_CountryOrderByGradeDesc(String countryName, Pageable pageable);
    List<Hotel> findAllByAddress_CityOrderByGradeDesc(String cityName, Pageable pageable);
    List<Hotel> findAllByAddress_CountryAndAddress_CityOrderByGradeDesc(String countryName, String cityName, Pageable pageable);
    List<Hotel> findAllByGradeOrderByGradeDesc(Integer gradeNumber, Pageable pageable);
}
