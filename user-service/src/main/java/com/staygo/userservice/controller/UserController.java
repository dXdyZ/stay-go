package com.staygo.userservice.controller;

import com.staygo.userservice.controller.documentation.UserControllerDocs;
import com.staygo.userservice.dto.AppointmentRequestDto;
import com.staygo.userservice.dto.UserDto;
import com.staygo.userservice.entity.Users;
import com.staygo.userservice.service.HotelAssignmentService;
import com.staygo.userservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Management user")
public class UserController implements UserControllerDocs {
    public final static String USER_PUBLIC_ID = "X-User-PublicId";

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

    @GetMapping("/by-publicId/{publicId}")
    public ResponseEntity<Users> getUserByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(userService.getUserByPublicId(publicId));
    }

    @PatchMapping("/{newUsername}/username")
    public ResponseEntity<Users> updateUsername(@PathVariable String newUsername,
                               @RequestHeader(USER_PUBLIC_ID) UUID publicId) {
        return ResponseEntity.ok(userService.updateUsername(publicId, newUsername));
    }

    @PatchMapping("/{phone}/phone")
    public ResponseEntity<Users> updatePhone(@PathVariable String phone,
                            @RequestHeader(USER_PUBLIC_ID) UUID publicId) {
        return ResponseEntity.ok(userService.updatePhoneNumber(publicId, phone));
    }

    @PatchMapping("/{email}/email")
    public ResponseEntity<Users> updateEmail(@PathVariable String email,
                             @RequestHeader(USER_PUBLIC_ID) UUID publicId) {
        return ResponseEntity.ok(userService.updateEmail(publicId, email));
    }

    @PatchMapping("/assign-hotel")
    public ResponseEntity<Users> appointmentHotel(
            @Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {

        return ResponseEntity.ok(hotelAssignmentService.appointmentHotel(appointmentRequestDto));
    }
}






