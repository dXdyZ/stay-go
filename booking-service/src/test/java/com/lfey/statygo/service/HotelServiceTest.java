package com.lfey.statygo.service;

import com.lfey.statygo.repository.HotelRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    HotelRepository hotelRepository;

    @InjectMocks
    HotelService hotelService;



}










