package com.lfey.statygo.service;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.CustomPageable;
import com.lfey.statygo.component.PriceCalculate;
import com.lfey.statygo.component.factory.HotelFactory;
import com.lfey.statygo.component.factory.PhotoDtoFactory;
import com.lfey.statygo.component.factory.RoomDtoFactory;
import com.lfey.statygo.dto.CreateHotelDto;
import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.dto.HotelUpdateRequestDto;
import com.lfey.statygo.dto.PhotoDto;
import com.lfey.statygo.entity.*;
import com.lfey.statygo.exception.HotelNotFoundException;
import com.lfey.statygo.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final RoomAvailabilityService roomAvailabilityService;
    private final PhotoService photoService;

    @Autowired
    public HotelService(HotelRepository hotelRepository, RoomAvailabilityService roomAvailabilityService,
                        PhotoService photoService) {
        this.hotelRepository = hotelRepository;
        this.roomAvailabilityService = roomAvailabilityService;
        this.photoService = photoService;
    }

    @Transactional
    public void saveHotel(CreateHotelDto createHotelDto) {
        //TODO STB-1
        Hotel hotel = Hotel.builder()
                    .stars(createHotelDto.getStars())
                    .name(createHotelDto.getName())
                    .address(Address.builder()
                            .city(createHotelDto.getCity())
                            .country(createHotelDto.getCountry())
                            .houseNumber(createHotelDto.getHouseNumber())
                            .street(createHotelDto.getStreet())
                            .postalCode(createHotelDto.getPostalCode())
                            .build())
                    .description(createHotelDto.getDescription())
            .build();
        photoService.uploadPhoto(createHotelDto.getPhotos(), hotel, createHotelDto.getMainPhotoIndex());
    }

    @Transactional
    public void addPhotoToHotel(List<MultipartFile> photos, Long hotelId, Integer mainPhotoIndex) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                () -> new HotelNotFoundException(String.format("Hotel by id: %s not found", hotelId))
        );
        photoService.uploadPhoto(photos, hotel, mainPhotoIndex);
    }

    public void updateHotelDataById(Long hotelId, HotelUpdateRequestDto hotelUpdateRequestDto) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                () -> new HotelNotFoundException(String.format("Hotel by id: %s not found", hotelId))
        );
        if (hotelUpdateRequestDto.getName() != null) {
            hotel.setName(hotelUpdateRequestDto.getName());
        }
        if (hotelUpdateRequestDto.getStars() != null) {
            hotel.setStars(hotelUpdateRequestDto.getStars());
        }
        if (hotelUpdateRequestDto.getDescription() != null) {
            hotel.setDescription(hotelUpdateRequestDto.getDescription());
        }
        hotelRepository.save(hotel);
    }

    //TODO STB-3
    @Transactional(readOnly = true)
    public HotelDto getHotelByIdUser(Long id, Integer guests, String startDate, String endDate) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(
                String.format("Hotel by id: %s not found", id)
        ));
        List<Room> freeRooms = roomAvailabilityService
                .getFreeRooms(hotel.getId(), startDate, endDate, null, guests, null);
        HotelDto hotelDto = HotelFactory.createHotelDto(hotel);
        Set<String> uniqueRoomType = new HashSet<>();
        if (!hotel.getRoom().isEmpty()) {
            hotelDto.getRoomDto().addAll(freeRooms.stream()
                    .map(room -> RoomDtoFactory.createRoomDto(room, startDate, endDate))
                    .filter(dto -> uniqueRoomType.add(dto.getRoomType()))
                    .toList());
        }
        return hotelDto;
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElseThrow(
                () -> new HotelNotFoundException(String.format("Hotel by id: %s not found", id))
        );
    }

    public void saveHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Transactional
    public void deleteHotelById(Long id) throws HotelNotFoundException{
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(
                String.format("Hotel by id: %s not found", id)));
        hotelRepository.delete(hotel);
    }

    //TODO WARNING Optimization methods for get room and photo
    @Transactional(readOnly = true)
    @Cacheable(value = "hotelSearch", key = "{#stars, #country, #city, #page, #startDate, #endDate}")
    public Page<HotelDto> searchHotelByFilter(String startDate, String endDate,
                                              Integer stars, String country, Double grade,
                                              String hotelType, Double minPrice, Double maxPrice,
                                              String city, int page) {

        Pageable pageable = CustomPageable.getPageable(page, 9);

        Page<Long> hotelIdPage = hotelRepository.findFilteredHotelIds(stars, country, city, pageable);

        Stream<HotelDto> hotelDtos = hotelRepository.findHotelsWithDetailsByIds(hotelIdPage.getContent()).stream()
                .flatMap(hotel -> createHotelDtoIfPossible(hotel, startDate, endDate).stream());


        if (hotelType != null && !hotelType.isEmpty()) {
            hotelDtos = hotelDtos.filter(hotel -> hotel.equals(hotelType));
        }
        if (minPrice != null) {
            hotelDtos = hotelDtos.filter(hotel -> hotel.getTotalPrice().compareTo(minPrice) >= 0);
        }
        if (maxPrice != null) {
            hotelDtos = hotelDtos.filter(hotel -> hotel.getTotalPrice().compareTo(maxPrice) <= 0);
        }
        if (grade != null) {
            hotelDtos = hotelDtos.filter(hotel -> hotel.getGrade().equals(grade));
        }


        return new PageImpl<>(hotelDtos.toList(), pageable, hotelIdPage.getTotalElements());
    }

    private Optional<HotelDto> createHotelDtoIfPossible(Hotel hotel, String startDate, String endDate) {
        Optional<Room> roomOptional;

        //TODO Решить проблему с получение только одной комнаты отеля а не всех
        if (hotel.getHotelType().equals(HotelType.HOTEL)) {
            roomOptional = hotel.getRoom().stream().filter(searchRoom -> searchRoom.getRoomType()
                            .equals(RoomType.STANDARD))
                    .findAny();
        } else {
            roomOptional = hotel.getRoom().stream().findFirst();
        }

        if (roomOptional.isEmpty()) return Optional.empty();

        Room room = roomOptional.get();
        HotelDto hotelDto = HotelFactory.createHotelDtoSearch(hotel);

        hotelDto.setTotalPrice(PriceCalculate.calculationTotalPrice(room.getPricePerDay(),
                CustomDateFormatter.localDateFormatter(startDate),
                CustomDateFormatter.localDateFormatter(endDate)));

        //TODO Решить вопрос с получением только главной фотографии из базы
        hotel.getMainPhoto()
                .map(PhotoDtoFactory::createPhotoDto)
                .ifPresent(photoDto -> hotelDto.getPhotoDto().add(photoDto));

        return Optional.of(hotelDto);
    }
}
