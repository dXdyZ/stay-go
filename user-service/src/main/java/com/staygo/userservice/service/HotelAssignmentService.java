package com.staygo.userservice.service;

import com.staygo.userservice.dto.AppointmentRequest;
import com.staygo.userservice.entity.Users;
import com.staygo.userservice.exception.DuplicateRoleException;
import com.staygo.userservice.exception.HotelNotFoundException;
import com.staygo.userservice.exception.UserNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;
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
    public Users appointmentHotel(AppointmentRequest appointmentRequest) throws UserNotFoundException,
            HotelNotFoundException, DuplicateRoleException {

        Users users = userService.getUserByUsername(appointmentRequest.username());
        bookingClientService.checkHotelExistsInBookingService(appointmentRequest.hotelId());

        users.setHotelId(appointmentRequest.hotelId());
        authClientService.addRoleInUserService(users.getUsername(), appointmentRequest.roleName());
        userService.saveUser(users);
        return users;
    }
}








