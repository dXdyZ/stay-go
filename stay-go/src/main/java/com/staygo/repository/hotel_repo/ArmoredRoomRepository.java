package com.staygo.repository.hotel_repo;

import com.staygo.enity.hotel.ArmoredRoom;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArmoredRoomRepository extends CrudRepository<ArmoredRoom, Long> {
    List<ArmoredRoom> findByDateArmoredAndDepartureDateAndRoom_Hotel_Address_City(String dateArmored, String departureDate, @NotNull @Size(
            min = 2,
            max = 255,
            message = "Требуетяс название города, максимум 255 символов"
    ) String room_hotel_address_city);
}
