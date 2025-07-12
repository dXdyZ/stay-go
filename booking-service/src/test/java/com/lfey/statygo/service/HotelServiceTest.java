package com.lfey.statygo.service;

import com.lfey.statygo.GenerationData;
import com.lfey.statygo.component.CustomPageable;
import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.entity.*;
import com.lfey.statygo.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    private static final Logger log = LoggerFactory.getLogger(HotelServiceTest.class);
    @Mock
    HotelRepository hotelRepository;

    @Mock
    RoomAvailabilityService roomAvailabilityService;

    @InjectMocks
    HotelService hotelService;

    @Test
    void searchHotelByFilter_ValidData_WhenUsageAllParametersForFilter() {
       var startDate = "2025-07-15";
       var endDate = "2025-07-16";
       var stars = 5;
       var country = "Russian";
       var city = "Moscow";
       var grade = 5.0;
       var hotelType = "HOTEL";
       var minPrice = 50.0;
       var maxPrice = 200.0;
       var page = 0;

       List<Hotel> data = GenerationData.generateHotel(10, country, city, grade, stars, HotelType.HOTEL,
               null, null, null);

       log.info("Vis data: {}", data);

       Pageable pageable = CustomPageable.getPageable(0, 9);

       doReturn(new PageImpl<>(data.stream().map(Hotel::getId).toList(), pageable, 10))
               .when(this.hotelRepository).findFilteredHotelIds(stars, country, city, pageable);

       doReturn(data).when(this.hotelRepository).findHotelsWithDetailsByIds(data.stream().map(Hotel::getId).toList());

       Page<HotelDto> result = this.hotelService.searchHotelByFilter(startDate, endDate, stars, country, grade,
               hotelType, minPrice, maxPrice, city, 0);

       assertNotNull(result);
       assertEquals(10, result.getTotalElements());
    }
}










