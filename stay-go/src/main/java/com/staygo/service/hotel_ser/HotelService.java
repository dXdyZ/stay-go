package com.staygo.service.hotel_ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.enity.Address;
import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.HotelData;
import com.staygo.enity.user.DTO.HotelDTO;
import com.staygo.repository.hotel_repo.HotelRepository;
import com.staygo.service.AddressService;
import com.staygo.service.user_ser.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final ArmoredRoomService armoredRoomService;

    @Autowired
    public HotelService(HotelRepository hotelRepository,
                        AddressService addressService,
                        UserService userService, ArmoredRoomService armoredRoomService) {
        this.hotelRepository = hotelRepository;
        this.addressService = addressService;
        this.userService = userService;
        this.armoredRoomService = armoredRoomService;
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
            if (!addressService.findAllParameterize(hotel.getAddress().getCountry(), hotel.getAddress().getCity(),
                    hotel.getAddress().getStreet(), hotel.getAddress().getZipCode())) {
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
        Pageable pageable = PageRequest.of(0, 5);
        List<Hotel> allHotelUser = hotelRepository.findAllByUsers_Username(principal.getName(), pageable);
        if (allHotelUser.isEmpty()) {
            return new ResponseEntity<>("У вас нет зарегестрированных отелей", HttpStatus.OK);
        } else return ResponseEntity.ok(allHotelUser);
    }

    public List<Hotel> allHotel() {
        Pageable pageable = PageRequest.of(0, 10);
        return hotelRepository.findAllByOrderByGradeDesc(pageable);
    }

    @Transactional
    @Cacheable("userFindAllHotelForArmored")
    public ResponseEntity<?> findAllHotelByCityAndDataArmoredAndTerm(String city, String dateArmored, String departureDate, Integer grade) {
        List<Hotel> hotels = hotelRepository.findAllByAddress_City(city, getPageable());
        List<Hotel> hotelGrade = new ArrayList<>();
        if (!hotels.isEmpty()) {
            try {
                for (Hotel hotel : hotels) {
                    if (grade != null) {
                        if (hotel.getGrade().equals(grade)) {
                            hotelGrade.add(hotel);
                        }
                    }
                    hotel.setRooms(hotel.getRooms().stream()
                            .filter(room -> !room.getArmoredRoom().getDateArmored().equals(dateArmored) &&
                                    !room.getArmoredRoom().getDepartureDate().equals(departureDate) &&
                                    room.getRoomStatus().equals("free"))
                            .collect(Collectors.toList()));
                    return ResponseEntity.ok(hotels);
                }
                if (!hotelGrade.isEmpty()) {
                    for (Hotel hotel : hotelGrade) {
                        hotel.setRooms(hotel.getRooms().stream()
                                .filter(room -> !room.getArmoredRoom().getDateArmored().equals(dateArmored) &&
                                        !room.getArmoredRoom().getDepartureDate().equals(departureDate) &&
                                        room.getRoomStatus().equals("free"))
                                .collect(Collectors.toList()));
                    }
                    return ResponseEntity.ok(hotelGrade);
                }
            } catch (NullPointerException e) {
                log.error("null pointer exception: {}", e.getMessage());
                return ResponseEntity.ok(hotels.stream().filter(n -> n.getGrade().equals(grade)).toList());
            }
        } return new ResponseEntity<>("В скором времени отели этого города появятся в нашем сервисе", HttpStatus.INTERNAL_SERVER_ERROR);
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

}
