package com.staygo.service.hotel_ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.custom_exception.DateException;
import com.staygo.enity.DTO.CommentsDTO;
import com.staygo.enity.DTO.RoomDTO;
import com.staygo.enity.DTO.rabbit.UserFindHotelDTO;
import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.HotelData;
import com.staygo.enity.DTO.HotelDTO;
import com.staygo.enity.hotel.Room;
import com.staygo.repository.hotel_repo.HotelRepository;
import com.staygo.service.AddressService;
import com.staygo.service.PayService;
import com.staygo.service.rabbit.RabbitMessage;
import com.staygo.service.user_ser.UserService;
import com.staygo.service.weather.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final PayService payService;
    private final WeatherService waetherService;
    private final RabbitMessage rabbitMessage;

    @Autowired
    public HotelService(HotelRepository hotelRepository,
                        AddressService addressService,
                        UserService userService, PayService payService, PayService payService1, WeatherService waetherService, RabbitMessage rabbitMessage) {
        this.hotelRepository = hotelRepository;
        this.addressService = addressService;
        this.userService = userService;
        this.payService = payService1;
        this.waetherService = waetherService;
        this.rabbitMessage = rabbitMessage;
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 10);
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
                        HotelDTO.builder()
                            .name(hotel.getName())
                            .grade(hotel.getGrade())
                            .country(hotel.getAddress().getCountry())
                            .city(hotel.getAddress().getCity())
                            .street(hotel.getAddress().getStreet())
                            .houseNumber(hotel.getAddress().getNumberHouse())
                            .rooms(sampleFirstByPrestige(hotel.getRooms()))
                            .build(),  HttpStatus.OK);
            } else return new ResponseEntity<>("Отель по этому адресу уже существет", HttpStatus.BAD_REQUEST);
        } catch (JsonMappingException e) {
            log.error("json mapping: {}", e.getMessage());
            return new ResponseEntity<>("Неправильно введенные данные", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            log.error("json processing: {}", e.getMessage());
            return new ResponseEntity<>("Неправильно введенные данные", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<RoomDTO> sampleFirstByPrestige(List<Room> rooms) {
        List<Room> roomList = rooms.stream()
                .collect(Collectors.toMap(
                        Room::getPrestige,
                        room -> room,
                        (existing, replacement) -> existing
                ))
                .values().stream()
                .toList();
        return roomList.stream().map(room -> new RoomDTO(room.getRoomName(),
                room.getRoomStatus(), room.getPrice(), room.getPrestige())).toList();
    }

    @Transactional
    @CachePut(value = "findAllHotelForArmored", key = "#hotelNew.address")
    public ResponseEntity<?> updateToDataHotel(HotelDTO hotelNew) {
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
            return ResponseEntity.ok(HotelDTO.builder()
                    .name(hotel.get().getName())
                    .grade(hotel.get().getGrade())
                    .rooms(sampleFirstByPrestige(hotel.get().getRooms()))
                    .street(hotel.get().getAddress().getStreet())
                    .city(hotel.get().getAddress().getCity())
                    .country(hotel.get().getAddress().getCountry())
                    .houseNumber(hotel.get().getAddress().getNumberHouse())
                    .build());
        } else return new ResponseEntity<>("Отеля с такими данными нету, либо добавьте его либо обратитесь в техподдержку", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getAllHotelUsers(Principal principal) {
        List<Hotel> allHotelUser = hotelRepository.findAllByUsers_Username(principal.getName(), getPageable());
        if (allHotelUser.isEmpty()) {
            return new ResponseEntity<>("У вас нет зарегистрированных отелей", HttpStatus.OK);
        } else return ResponseEntity.ok(allHotelUser);
    }

    public List<Hotel> allHotel() {
        return hotelRepository.findAllByOrderByGradeDesc(getPageable());
    }

    @Transactional
    @Cacheable(value = "findAllHotelForArmored",  key = "#country + '-' + #city + '-' + #dateArmored + '-' + #departureDate + '-' + (#grade != null ? #grade : 'null')") //key смотри на то по чему мы будем кэшировать
    public List<HotelDTO> findAllHotelByCityAndDataArmoredAndTerm(String country, String city,
                                                                  String dateArmored, String departureDate,
                                                                  Integer grade, Principal principal) {
        if (principal != null) {
            rabbitMessage.sendDataUserFindHotel(new UserFindHotelDTO(city, country, dateArmored, departureDate, grade,
                    principal.getName()));
        } else {
            rabbitMessage.sendDataUserFindHotel(new UserFindHotelDTO(city, country, dateArmored, departureDate, grade,
                    "no auth user"));
        }

        List<Hotel> hotels;

        try {
            if (grade == null) {
                hotels = hotelRepository.findAllByAddress_CityAndAddress_Country(city, country ,getPageable());
                return addingPriceToTheHotel(hotels, dateArmored, departureDate);

            } else {
                hotels = hotelRepository.findAllByGradeAndAddress_CityAndAddress_Country(grade, city, country ,getPageable());
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

    @Transactional
    public Hotel findByCityAndNameAndStreet(String street, String city, String name) {
        return hotelRepository.findByAddress_CityAndNameAndAddress_Street(city, name, street);
    }

    @Transactional
    public Optional<Hotel> findByUserAndStreet(String street, String username) {
        if (!username.isEmpty() && !street.isEmpty()) {
            return hotelRepository.findByUsers_UsernameAndAddress_Street(username, street);
        } else throw new NullPointerException("Поля не должны быть пустыми");
    }

    @Transactional
    public void deleteAllHotel() {
        hotelRepository.deleteAll();
    }


    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }

    private List<HotelDTO> addingPriceToTheHotel(List<Hotel> hotels, String dateArmored, String departureDate) throws ParseException, DateException {
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
            hotelDTO.add(HotelDTO.builder()
                    .name(hotel.getName())
                    .grade(hotel.getGrade())
                    .comments(hotel.getComments().stream().map(comments -> new CommentsDTO(comments.getComment(),
                            comments.getCreatDate(), comments.getUsers().getUsername(), comments.getHotel().getName())).toList())
                    .rooms(sampleFirstByPrestige(hotel.getRooms()))
                    .city(hotel.getAddress().getCity())
                    .country(hotel.getAddress().getCountry())
                    .street(hotel.getAddress().getStreet())
                    .houseNumber(hotel.getAddress().getNumberHouse())
                    .allPrice(String.valueOf(Objects.requireNonNull(price).subtract(BigDecimal.valueOf(count))))
                    .weather( waetherService.sortedTimeByDay(dateArmored, departureDate, hotel.getAddress().getCity(), hotel.getAddress().getCountry()))
                    .build());
        }
        return hotelDTO;
    }


    private ResponseEntity<?> addingRoomsToAHotel(List<Hotel> hotels, String dateArmored, String departureDate) {
        List<HotelDTO> returnHotelDTO = new ArrayList<>();
        List<Hotel> hotelsNew = new ArrayList<>();
        try {
            if (!hotels.isEmpty()) {
                for (Hotel hotel : hotels) {
                    hotel.setRooms(hotel.getRooms().stream()
                            .filter(room -> !room.getArmoredRoom().iterator().next().getDateArmored().equals(dateArmored))
                            .filter(room -> !room.getArmoredRoom().iterator().next().getDepartureDate().equals(departureDate))
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
