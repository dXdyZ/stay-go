package com.staygo.userservice.service;

import com.staygo.userservice.dto.AppointmentRequestDto;
import com.staygo.userservice.entity.Users;
import com.staygo.userservice.exception.DuplicateRoleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelAssignmentServiceTest {

    @Mock
    UserService userService;

    @Mock
    AuthClientService authClientService;

    @Mock
    BookingClientService bookingClientService;

    @InjectMocks
    HotelAssignmentService hotelAssignmentService;

    @Test
    void appointmentHotel_WhenExistsUserAndHotelAndNotDuplicatedRole() {
        var username = "test";
        var hotelId = 1L;
        var roleName = "MANAGER";
        var appointmentRequest = new AppointmentRequestDto(username, hotelId, roleName);
        var users = Users.builder()
                .username(username)
                .build();
        doReturn(true).when(this.bookingClientService).validateHotelExists(hotelId);
        doReturn(users).when(this.userService).getUserByUsername(username);

        hotelAssignmentService.appointmentHotel(appointmentRequest);

        verify(this.userService).getUserByUsername(username);
        verify(this.bookingClientService).validateHotelExists(hotelId);
        verify(this.authClientService).addRoleInUserService(username, roleName);
        verify(this.userService).saveUser(argThat((Users actUser) -> {
            return actUser.getHotelId().equals(hotelId);
        }));
    }

    @Test
    void appointmentHotel_WhenExistsUserAndHotelButDuplicatedRole() {
        var username = "test";
        var hotelId = 1L;
        var roleName = "MANAGER";
        var appointmentRequest = new AppointmentRequestDto(username, hotelId, roleName);
        var users = Users.builder()
                .username(username)
                .build();
        doReturn(users).when(this.userService).getUserByUsername(username);
        doThrow(new DuplicateRoleException(
                String.format("Role: %s the user already has it", roleName)))
                .when(this.authClientService).addRoleInUserService(username, roleName);

        DuplicateRoleException exception = assertThrows(
                DuplicateRoleException.class,
                () -> this.hotelAssignmentService.appointmentHotel(appointmentRequest)
        );

        verify(this.userService, never()).saveUser(argThat((Users actUser) -> {
            return actUser.getHotelId().equals(hotelId);
        }));
    }
}






