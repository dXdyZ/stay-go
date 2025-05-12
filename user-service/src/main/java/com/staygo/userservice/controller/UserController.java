package com.staygo.userservice.controller;

import com.staygo.userservice.controller.documentation.UserControllerDocs;
import com.staygo.userservice.dto.AppointmentRequestDto;
import com.staygo.userservice.dto.UserDto;
import com.staygo.userservice.entity.Users;
import com.staygo.userservice.service.HotelAssignmentService;
import com.staygo.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {
    public final static String USERNAME_HEADER = "X-User-Username";

    private final UserService userService;
    private final HotelAssignmentService hotelAssignmentService;

    @Autowired
    public UserController(UserService userService, HotelAssignmentService hotelAssignmentService) {
        this.userService = userService;
        this.hotelAssignmentService = hotelAssignmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/save")
    public void saveUser(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
    }

    @GetMapping("/by-name/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<Users> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/by-phone/{phone}")
    public ResponseEntity<Users> getUserByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(userService.getUserByPhoneNumber(phone));
    }

    @PatchMapping("/{newUsername}/username")
    public ResponseEntity<Users> updateUsername(@PathVariable String newUsername,
                               @RequestHeader(USERNAME_HEADER) String username) {
        return ResponseEntity.ok(userService.updateUsername(username, newUsername));
    }

    @PatchMapping("/{phone}/phone")
    public ResponseEntity<Users> updatePhone(@PathVariable String phone,
                            @RequestHeader(USERNAME_HEADER) String username) {
        return ResponseEntity.ok(userService.updatePhoneNumber(username, phone));
    }

    @PatchMapping("/{email}/email")
    public ResponseEntity<Users> updateEmail(@PathVariable String email,
                             @RequestHeader(USERNAME_HEADER) String username) {
        return ResponseEntity.ok(userService.updateEmail(email, username));
    }

    @PatchMapping("/assign-hotel")
    public ResponseEntity<Users> appointmentHotel(
            @Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {

        return ResponseEntity.ok(hotelAssignmentService.appointmentHotel(appointmentRequestDto));
    }
}






