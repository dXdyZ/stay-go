package com.lfey.statygo.service;

import com.lfey.statygo.GenerationData;
import com.lfey.statygo.component.CustomPageable;
import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.entity.HotelType;
import com.lfey.statygo.repository.jpaRepository.HotelRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
       var startDate = LocalDate.now().toString();
       var endDate = LocalDate.now().plusDays(1).toString();
       var stars = 5;
       var country = "Russian";
       var city = "Moscow";
       var grade = 5.0;
       var hotelType = "HOTEL";
       var minPrice = 50.0;
       var maxPrice = 200.0;

       List<Hotel> data = GenerationData.generateHotel(10, country, city, grade, stars, HotelType.HOTEL,
               null, null, null, maxPrice, minPrice, null);

       Pageable pageable = CustomPageable.getPageable(0, 9);

       doReturn(new PageImpl<>(data.stream().map(Hotel::getId).toList(), pageable, 10))
               .when(this.hotelRepository).findFilteredHotelIds(stars, country, city, pageable);

       doReturn(data).when(this.hotelRepository).findHotelsWithDetailsByIds(data.stream().map(Hotel::getId).toList());

       Page<HotelDto> result = this.hotelService.searchHotelByFilter(startDate, endDate, stars, country, grade,
               hotelType, minPrice, maxPrice, city, 0);


       assertNotNull(result, "Result empty");
       assertTrue(result.hasContent());
       result.getContent().forEach(hotel -> {
           assertAll(() -> assertTrue(hotel.getTotalPrice() >= minPrice, "There are no matches on the minimum boundary"),
                   () -> assertTrue(hotel.getTotalPrice() <= maxPrice, "There are no matches on the maximum boundary"));
       });
    }

    @Test
    void searchHotelByFilter_ValidData_WhenStartDateButDontUsageEndDate() {
        var startDate = LocalDate.now().toString();
        var country = "Russian";
        var city = "Moscow";

        Pageable pageable = CustomPageable.getPageable(0, 9);

        List<Hotel> data = GenerationData.generateHotel(10, country, city, null, null, HotelType.HOTEL,
                null, null, null, null, null, 100.0);

        doReturn(new PageImpl<>(data.stream().map(Hotel::getId).toList(), pageable, 5))
                .when(this.hotelRepository).findFilteredHotelIds(null, country, city, pageable);

        doReturn(data).when(this.hotelRepository).findHotelsWithDetailsByIds(data.stream().map(Hotel::getId).toList());

        Page<HotelDto> result = this.hotelService.searchHotelByFilter(startDate, null, null, country,
                null, null, null, null, city, 0);

        assertNotNull(result);
        assertTrue(result.hasContent());
        result.getContent().forEach(hotel -> {
            assertAll(() -> assertEquals(700.0, hotel.getTotalPrice()));
        });

    }

    @Test
    void test() {
        var averageRating = 4.0;
        List<Integer> gradePerDay = new ArrayList<>() {
            {
                add(1);
                add(4);
                add(3);
                add(4);
                add(5);
            }
        };

        int dayResult = 0;

        for (Integer nums : gradePerDay) {
            dayResult += nums;
        }

        double result = (averageRating * 10 + dayResult) / (10 + gradePerDay.size());

        log.info("Algos result: {}", result);
    }
}










