package com.staygo.service.hotel_ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.castom_exe.DateException;
import com.staygo.enity.Address;
import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.HotelData;
import com.staygo.enity.DTO.HotelDTO;
import com.staygo.enity.hotel.Room;
import com.staygo.repository.hotel_repo.HotelRepository;
import com.staygo.service.AddressService;
import com.staygo.service.PayService;
import com.staygo.service.user_ser.UserService;
import com.staygo.service.weather.WaetherService;
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
import java.net.URISyntaxException;
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
    private final WaetherService waetherService;

    @Autowired
    public HotelService(HotelRepository hotelRepository,
                        AddressService addressService,
                        UserService userService, PayService payService, PayService payService1, WaetherService waetherService) {
        this.hotelRepository = hotelRepository;
        this.addressService = addressService;
        this.userService = userService;
        this.payService = payService1;
        this.waetherService = waetherService;
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
                            .address(hotel.getAddress())
                            .rooms(hotel.getRooms())
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

    @Transactional
    @CachePut(value = "findAllHotelForArmored", key = "#hotelNew.address")
    public ResponseEntity<?> updateToDataHotel(HotelDTO hotelNew) {
        Optional<Hotel> hotel = hotelRepository.findByAddress_CountryAndAddress_CityAndAddress_Street(hotelNew.getAddress().getCountry(), hotelNew.getAddress().getCity(), hotelNew.getAddress().getStreet());
        if (hotel.isPresent()) {
            if (hotelNew.getName() != null) {
                hotel.get().setName(hotelNew.getName());
            }
            if (hotelNew.getGrade() != null) {
                hotel.get().setGrade(hotelNew.getGrade());
            }
            if (hotelNew.getAddress() != null) {
                Address existingAddress = hotel.get().getAddress();
                Address newAddress = hotelNew.getAddress();

                if (newAddress.getCountry() != null) existingAddress.setCountry(newAddress.getCountry());
                if (newAddress.getCity() != null) existingAddress.setCity(newAddress.getCity());
                if (newAddress.getStreet() != null) existingAddress.setStreet(newAddress.getStreet());
                if (newAddress.getZipCode() != null) existingAddress.setZipCode(newAddress.getZipCode());
            }
            hotelRepository.save(hotel.get());
            HotelDTO returnHotel = HotelDTO.builder()
                    .name(hotel.get().getName())
                    .grade(hotel.get().getGrade())
                    .rooms(hotel.get().getRooms())
                    .address(hotel.get().getAddress())
                    .build();
            return ResponseEntity.ok(returnHotel);
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
    @Cacheable("findAllHotelForArmored")
    public ResponseEntity<?> findAllHotelByCityAndDataArmoredAndTerm(String country, String city, String dateArmored, String departureDate, Integer grade) {
        List<Hotel> hotels;
        try {
            if (grade == null) {
                hotels = hotelRepository.findAllByAddress_CityAndAddress_Country(city, country ,getPageable());
                return ResponseEntity.ok(addingPriceToTheHotel(hotels, dateArmored, departureDate) + "\n" + waetherService.sortedTimeByDay(dateArmored, departureDate, city, country));
            } else {
                hotels = hotelRepository.findAllByGradeAndAddress_CityAndAddress_Country(grade, city, country ,getPageable());
                return ResponseEntity.ok(addingPriceToTheHotel(hotels, dateArmored, departureDate) + "\n" + waetherService.sortedTimeByDay(dateArmored, departureDate, city, country));
            }
        } catch (ParseException e) {
            log.error("parse exception in method findAllHotelByCityAndDataArmoredAndTerm with data: {}", city,
                    dateArmored,
                    departureDate,
                    grade, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DateException e) {
            log.error("fatal user data: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Hotel findByCityAndName(String city, String name) {
        return hotelRepository.findByAddress_CityAndName(city, name);
    }

    @Transactional
    public Optional<Hotel> findByUserAndStreet(String street, String username) {
        if (!username.isEmpty() && !street.isEmpty()) {
            return hotelRepository.findByUsers_UsernameAndAddress_Street(username, street);
        } else throw new NullPointerException("Поля не должны быть путыми");
    }

    @Transactional
    public void deleteAllHotel() {
        hotelRepository.deleteAll();
    }


    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }

    private HashMap<HotelDTO, BigDecimal> addingPriceToTheHotel(List<Hotel> hotels, String dateArmored, String departureDate) throws ParseException, DateException {
        HashMap<HotelDTO, BigDecimal> hotelAndAvgPrice = new HashMap<>();
        BigDecimal price = null;
        long count = 0;
        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRooms()) {
                log.info("room price: {}", room.getPrice());
                price = payService.costCalculation(dateArmored, departureDate, room.getPrice());
                log.info("information working method costCalculation: {}", payService.costCalculation(dateArmored, departureDate, room.getPrice()));
                count++;
                log.info("count: {}", count);
            }
            hotelAndAvgPrice.put(HotelDTO.builder()
                    .name(hotel.getName())
                    .grade(hotel.getGrade())
                    .comments(hotel.getComments())
                    .rooms(hotel.getRooms())
                    .address(hotel.getAddress())
                    .build(), Objects.requireNonNull(price).subtract(BigDecimal.valueOf(count)));
        }
        return hotelAndAvgPrice;
    }


    private ResponseEntity<?> addingRoomsToAHotel(List<Hotel> hotels, String dateArmored, String departureDate) {
        List<HotelDTO> returnHotelDTO = new ArrayList<>();
        List<Hotel> hotelsNew = new ArrayList<>();
        try {
            if (!hotels.isEmpty()) {
                for (Hotel hotel : hotels) {
                    hotel.setRooms(hotel.getRooms().stream()
                            .filter(room -> !room.getArmoredRoom().getDateArmored().equals(dateArmored) &&
                                    !room.getArmoredRoom().getDepartureDate().equals(departureDate) &&
                                    room.getRoomStatus().equals("free"))
                            .collect(Collectors.toList()));
                    hotelsNew.add(hotel);
                }
                returnHotelDTO.addAll(hotelsNew.stream()
                        .map(hotelReturn -> HotelDTO.builder()
                                .rooms(hotelReturn.getRooms())
                                .comments(hotelReturn.getComments())
                                .grade(hotelReturn.getGrade())
                                .name(hotelReturn.getName())
                                .address(hotelReturn.getAddress())
                                .build())
                        .toList());
                return ResponseEntity.ok(returnHotelDTO);
            } return new ResponseEntity<>("В скором времени отели этого города появятся в нашем сервисе", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException e) {
            log.error("null pointer exception: {}", e.getMessage());
            returnHotelDTO.addAll(hotels.stream()
                    .map(hotelReturn -> HotelDTO.builder()
                            .rooms(hotelReturn.getRooms())
                            .comments(hotelReturn.getComments())
                            .grade(hotelReturn.getGrade())
                            .name(hotelReturn.getName())
                            .address(hotelReturn.getAddress())
                            .build())
                    .toList());
            return ResponseEntity.ok(returnHotelDTO);
        }
    }
}
