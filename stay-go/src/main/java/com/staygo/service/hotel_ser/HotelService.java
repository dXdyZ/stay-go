package com.staygo.service.hotel_ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.component.ComponentForCreateHotelDTO;
import com.staygo.component.Page;
import com.staygo.enity.DTO.HotelDTO;
import com.staygo.enity.DTO.rabbit.UserFindHotelDTO;
import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.HotelData;
import com.staygo.repository.hotel_repo.HotelRepository;
import com.staygo.service.AddressService;
import com.staygo.service.hotel_ser.cached.HotelCachedService;
import com.staygo.service.rabbit.RabbitMessage;
import com.staygo.service.user_ser.UserService;
import com.staygo.service.weather.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final WeatherService weatherService;
    private final RabbitMessage rabbitMessage;
    private final HotelCachedService hotelCachedService;
    private final ComponentForCreateHotelDTO componentForCreateHotelDTO;

    @Autowired
    public HotelService(HotelRepository hotelRepository, AddressService addressService,
                        UserService userService, WeatherService waetherService,
                        RabbitMessage rabbitMessage, HotelCachedService hotelCachedService,
                        ComponentForCreateHotelDTO componentForCreateHotelDTO) {
        this.hotelRepository = hotelRepository;
        this.addressService = addressService;
        this.userService = userService;
        this.weatherService = waetherService;
        this.rabbitMessage = rabbitMessage;
        this.hotelCachedService = hotelCachedService;
        this.componentForCreateHotelDTO = componentForCreateHotelDTO;
    }

    @Transactional
    public ResponseEntity<?> createdHotel(String hotelJson, List<MultipartFile> hotelDataFiles, Principal principal) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Hotel hotel = objectMapper.readValue(hotelJson, Hotel.class);
            hotel.setUsers(userService.findByName(principal.getName()).get());
            log.info("hotel: {}", hotel);
            if (!addressService.findAllParameterize(hotel.getAddress().getStreet(), hotel.getAddress().getNumberHouse(), hotel.getAddress().getZipCode())) {
                if (hotelDataFiles != null) {
                    try {
                        List<HotelData> hotelDataList = new ArrayList<>();
                        for (MultipartFile hotelFiles : hotelDataFiles) {
                            byte[] fileBytes = hotelFiles.getBytes();
                            String fileName = hotelFiles.getOriginalFilename();
                            hotelDataList.add(HotelData.builder()
                                    .name(fileName)
                                    .data(fileBytes)
                                    .createDate(new Date())
                                    .hotel(hotel)
                                    .build());
                        }
                        hotel.setHotelData(hotelDataList);
                    } catch (Exception e) {
                        log.error("exception: {}", e.getMessage());
                        return new ResponseEntity<>("Ошибка при добавление фото" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    log.info("hotel before: {}", hotel.toString());
                }

                hotelRepository.save(hotel);

                return new ResponseEntity<>("Для добавления фото к каждой комнате перейдите на следующую страницу '/addedRoom/Улица'. Также при добавлении фото к комнотам пожалуйста именнуйте файлы  n" +
                        componentForCreateHotelDTO.getHotelDTO(hotel),  HttpStatus.OK);
            } else return new ResponseEntity<>("Отель по этому адресу уже существет", HttpStatus.BAD_REQUEST);
        } catch (JsonMappingException e) {
            log.error("json mapping: {}", e.getMessage());
            return new ResponseEntity<>("Неправильно введенные данные", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            log.error("json processing: {}", e.getMessage());
            return new ResponseEntity<>("Неправильно введенные данные", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @CachePut(value = "findAllHotelForArmored",  key = "#country + '-' + #city + '-' + #dateArmored + '-' + #departureDate + '-' + (#grade != null ? #grade : 'null')")
    public HotelDTO updateToDataHotel(HotelDTO hotelNew) {
        Optional<Hotel> hotel = hotelRepository.findByAddress_CountryAndAddress_CityAndAddress_Street(hotelNew.getCountry(),
                hotelNew.getCity(), hotelNew.getStreet());
        if (hotel.isPresent()) {
            if (hotelNew.getName() != null) {
                hotel.get().setName(hotelNew.getName());
            }
            if (hotelNew.getGrade() != null) {
                hotel.get().setGrade(hotelNew.getGrade());
            }
            if (!hotelNew.getCity().isBlank()) {
                hotel.get().getAddress().setCity(hotelNew.getCity());
            }
            if (!hotelNew.getCountry().isBlank()) {
                hotel.get().getAddress().setCountry(hotelNew.getCountry());
            }
            if (!hotelNew.getStreet().isBlank()) {
                hotel.get().getAddress().setStreet(hotelNew.getStreet());
            }
            if (!hotelNew.getHouseNumber().isBlank()) {
                hotel.get().getAddress().setStreet(hotelNew.getHouseNumber());
            }
            hotelRepository.save(hotel.get());
            return componentForCreateHotelDTO.getHotelDTO(hotel.get());
        } else {
            throw new NullPointerException();
        }
    }

    public ResponseEntity<?> getAllHotelUsers(Principal principal, Integer pageSize) {
        List<Hotel> allHotelUser = hotelRepository.findAllByUsers_Username(principal.getName(), Page.getPageable(pageSize));
        if (allHotelUser.isEmpty()) {
            return new ResponseEntity<>("У вас нет зарегистрированных отелей", HttpStatus.OK);
        } else return ResponseEntity.ok(allHotelUser);
    }

    public List<Hotel> allHotel(Integer pageSize) {
        return hotelRepository.findAllByOrderByGradeDesc(Page.getPageable(pageSize));
    }

    public List<HotelDTO> findHotelForSendMessage(String country, String city,
                                                  String dateArmored, String departureDate,
                                                  Integer grade, Principal principal, Integer pageSize) {
        if (principal != null) {
            rabbitMessage.sendDataUserFindHotel(new UserFindHotelDTO(city, country, dateArmored, departureDate, grade,
                    principal.getName()));
        } else {
            rabbitMessage.sendDataUserFindHotel(new UserFindHotelDTO(city, country, dateArmored, departureDate, grade,
                    "no auth user"));
        }

        Map<String, Integer> resultMethodCalculationWeather = weatherService.sortedTimeByDay(dateArmored, departureDate, city, country);
        return hotelCachedService.findAllHotelByCityAndDataArmoredAndTerm(country, city, dateArmored, departureDate, grade, pageSize).stream()
                .peek(hotel -> hotel.setWeather(resultMethodCalculationWeather))
                .toList();
    }


    @Transactional
    public Hotel findByCityAndNameAndStreet(String street, String city, String name) {
        return hotelRepository.findByAddress_CityAndNameAndAddress_Street(city, name, street);
    }

    @Transactional
    public Optional<Hotel> findByUserAndStreetAndCityAndCountry(String city, String country, String hotelName, String street, String username) {
        if (!username.isBlank() && !street.isBlank() && !city.isBlank() && !country.isBlank() && !hotelName.isBlank()) {
            return hotelRepository.findByNameAndAddress_CityAndAddress_CountryAndAddress_StreetAndUsers_Username(hotelName, city, country, street, username);
        } else throw new NullPointerException("Поля не должны быть пустыми");
    }

    @Transactional
    public void deleteAllHotel() {
        hotelRepository.deleteAll();
    }


    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }


    private ResponseEntity<?> addingRoomsToAHotel(List<Hotel> hotels, String dateArmored, String departureDate) {
        List<HotelDTO> returnHotelDTO = new ArrayList<>();
        List<Hotel> hotelsNew = new ArrayList<>();
        try {
            if (!hotels.isEmpty()) {
                for (Hotel hotel : hotels) {
                    hotel.setRooms(hotel.getRooms().stream()
                            .filter(room -> room.getArmoredRoom() != null && room.getArmoredRoom().stream().allMatch(armoredRoom ->
                                    !armoredRoom.getDateArmored().equals(dateArmored) &&
                                            !armoredRoom.getDepartureDate().equals(departureDate)
                            ))
                            .collect(Collectors.toList()));
                    hotelsNew.add(hotel);
                }
                returnHotelDTO.addAll(hotelsNew.stream()
                        .map(hotelReturn -> HotelDTO.builder()
                                //.rooms(hotelReturn.getRooms())
                               // .comments(hotelReturn.getComments())
                                .grade(hotelReturn.getGrade())
                                .name(hotelReturn.getName())
                               // .address(hotelReturn.getAddress())
                                .build())
                        .toList());
                return ResponseEntity.ok(returnHotelDTO);
            } return new ResponseEntity<>("В скором времени отели этого города появятся в нашем сервисе", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException e) {
            log.error("null pointer exception: {}", e.getMessage());
            returnHotelDTO.addAll(hotels.stream()
                    .map(hotelReturn -> HotelDTO.builder()
                            //.rooms(hotelReturn.getRooms())
                            //.comments(hotelReturn.getComments())
                            .grade(hotelReturn.getGrade())
                            .name(hotelReturn.getName())
                            //.address(hotelReturn.getAddress())
                            .build())
                    .toList());
            return ResponseEntity.ok(returnHotelDTO);
        }
    }
}
