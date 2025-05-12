package com.staygo.userservice.service;

import com.staygo.userservice.dto.AppointmentRequestDto;
import com.staygo.userservice.entity.Users;
import com.staygo.userservice.exception.DuplicateRoleException;
import com.staygo.userservice.exception.HotelNotFoundException;
import com.staygo.userservice.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelAssignmentService {
    private final UserService userService;
    private final AuthClientService authClientService;
    private final BookingClientService bookingClientService;


    public HotelAssignmentService(UserService userService, AuthClientService authClientService,
                                  BookingClientService bookingClientService) {
        this.userService = userService;
        this.authClientService = authClientService;
        this.bookingClientService = bookingClientService;
    }

    /**
     * TODO сделать запрос проверки существования отеля к booking service
     * TODO сделать запрос к auth service для проверки роли пользователя
     */
    @Transactional
    public Users appointmentHotel(AppointmentRequestDto appointmentRequestDto) throws UserNotFoundException,
            HotelNotFoundException, DuplicateRoleException {

        Users users = userService.getUserByUsername(appointmentRequestDto.username());
        bookingClientService.validateHotelExists(appointmentRequestDto.hotelId());

        users.setHotelId(appointmentRequestDto.hotelId());
        authClientService.addRoleInUserService(users.getUsername(), appointmentRequestDto.roleName());
        userService.saveUser(users);
        return users;
    }
}








