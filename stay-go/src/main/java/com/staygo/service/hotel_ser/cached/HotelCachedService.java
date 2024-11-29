package com.staygo.service.hotel_ser.cached;

import com.staygo.component.Page;
import com.staygo.custom_exception.DateException;
import com.staygo.enity.DTO.CommentsDTO;
import com.staygo.enity.DTO.HotelDTO;
import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.Room;
import com.staygo.repository.hotel_repo.HotelRepository;
import com.staygo.service.PayService;
import com.staygo.component.ComponentForCreateHotelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class HotelCachedService {
    private final HotelRepository hotelRepository;
    private final PayService payService;
    private final ComponentForCreateHotelDTO componentForCreateHotelDTO;

    @Autowired
    public HotelCachedService(HotelRepository hotelRepository, PayService payService, ComponentForCreateHotelDTO componentForCreateHotelDTO) {
        this.hotelRepository = hotelRepository;
        this.payService = payService;
        this.componentForCreateHotelDTO = componentForCreateHotelDTO;
    }

    @Transactional
    @Cacheable(value = "findAllHotelForArmored",  key = "#country + '-' + #city + '-' + #dateArmored + '-' + #departureDate + '-' + (#grade != null ? #grade : 'null')") //key смотри на то по чему мы будем кэшировать
    public List<HotelDTO> findAllHotelByCityAndDataArmoredAndTerm(String country, String city,
                                                                  String dateArmored, String departureDate,
                                                                  Integer grade, Integer pageSize) {
        log.info("Cache key: {}", country + '-' + city + '-' + dateArmored + '-' + departureDate + '-' + (grade != null ? grade : "null"));
        List<Hotel> hotels;
        try {
            if (grade == null) {
                hotels = hotelRepository.findAllByAddress_CityAndAddress_Country(city, country, Page.getPageable(pageSize));
                return addingPriceToTheHotel(hotels, dateArmored, departureDate);

            } else {
                hotels = hotelRepository.findAllByGradeAndAddress_CityAndAddress_Country(grade, city, country, Page.getPageable(pageSize));
                return addingPriceToTheHotel(hotels, dateArmored, departureDate);
            }
        } catch (ParseException e) {
            log.error("parse exception in method findAllHotelByCityAndDataArmoredAndTerm with data: {}", city,
                    dateArmored,
                    departureDate,
                    grade, e.getMessage());
            return null;
        } catch (DateException e) {
            log.error("fatal user data: {}", e.getMessage());
            return null;
        }
    }

    public List<HotelDTO> addingPriceToTheHotel(List<Hotel> hotels, String dateArmored, String departureDate) throws ParseException, DateException {
        BigDecimal price = null;
        List<HotelDTO> hotelDTO = new ArrayList<>();
        long count = 0;
        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRooms()) {
                log.info("room price: {}", room.getPrice());
                price = payService.costCalculation(dateArmored, departureDate, room.getPrice());
                log.info("information working method costCalculation: {}", payService.costCalculation(dateArmored, departureDate, room.getPrice()));
                count++;
                log.info("count: {}", count);
            }
            hotelDTO.add(componentForCreateHotelDTO.getHotelDTO(hotel).builder()
                            .name(hotel.getName())
                            .grade(hotel.getGrade())
                            .city(hotel.getAddress().getCity())
                            .country(hotel.getAddress().getCountry())
                            .street(hotel.getAddress().getStreet())
                            .houseNumber(hotel.getAddress().getNumberHouse())
                            .comments(hotel.getComments().stream().map(comments -> new CommentsDTO(comments.getComment(),
                    comments.getCreatDate(), comments.getUsers().getUsername(), comments.getHotel().getName())).toList())
                            .rooms(componentForCreateHotelDTO.sampleFirstByPrestige(hotel.getRooms()))
                            .allPrice(String.valueOf(Objects.requireNonNull(price).subtract(BigDecimal.valueOf(count))))
                    .build());
        }
        return hotelDTO;
    }
}
