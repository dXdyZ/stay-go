package com.staygo.userservice.controller.documentation;

import jakarta.validation.Valid;
import com.staygo.userservice.dto.AppointmentRequestDto;
import com.staygo.userservice.dto.UserDto;
import com.staygo.userservice.entity.Users;
import com.staygo.userservice.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@OpenAPIDefinition
public interface UserControllerDocs {

    String USER_PUBLIC_ID = "X-User-PublicId";

    @Operation(
            summary = "User by id",
            description = "Getting user by id",
            responses = @ApiResponse(responseCode = "200", description = "Success response",
                    content = @Content(schema = @Schema(implementation = Users.class))
            )
    )
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                                name = "User by id not found",
                                value = """
                                        {
                                            "timestamp": "2024-07-15T14:30:45Z",
                                            "error": {
                                                "message": "User by id: 1 not found"
                                            },
                                            "code": 404
                                        }
                                        """
                        )
                    }
            )
    )
    @GetMapping("/{id}")
    ResponseEntity<Users> getUserById(@Parameter(description = "User id", example = "1") @PathVariable Long id);

    @Operation(
            summary = "Save user",
            description = "Save user after register",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            responses = @ApiResponse(responseCode = "200", description = "Success response")
    )
    @PostMapping("/save")
    void saveUser(@RequestBody UserDto userDto);

    @Operation(
            summary = "User by name",
            description = "Getting user by name",
            responses = @ApiResponse(responseCode = "200", description = "Success response",
                    content = @Content(schema = @Schema(implementation = Users.class))
            )
    )
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                                name = "User by name not found",
                                value = """
                                        {
                                            "timestamp": "2024-07-15T14:30:45Z",
                                            "error": {
                                                "message": "User by name: user not found"
                                            },
                                            "code": 404
                                        }
                                        """
                        )
                    }
            )
    )
    @GetMapping("/by-name/{username}")
    ResponseEntity<Users> getUserByUsername(
            @Parameter(description = "The name of the user we are looking for", example = "user", required = true)
            @PathVariable String username);


    @Operation(
            summary = "User by email",
            description = "Getting user by email",
            responses = @ApiResponse(responseCode = "200", description = "Success response",
                    content = @Content(schema = @Schema(implementation = Users.class))
            )
    )
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                                name = "User by name not found",
                                value = """
                                        {
                                            "timestamp": "2024-07-15T14:30:45Z",
                                            "error": {
                                                "message": "User by email: user@email.com not found"
                                            },
                                            "code": 404
                                        }
                                        """
                        )
                    }
            )
    )
    @GetMapping("/by-email/{email}")
    ResponseEntity<Users> getUserByEmail(
            @Parameter(description = "The email of the user we are looking for",
                    example = "user@email.com", required = true)
            @PathVariable String email);



    @Operation(
            summary = "User by phone number",
            description = "Getting user by phone number",
            responses = @ApiResponse(responseCode = "200", description = "Success response",
                    content = @Content(schema = @Schema(implementation = Users.class))
            )
    )
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                                name = "User by phone number not found",
                                value = """
                                        {
                                            "timestamp": "2024-07-15T14:30:45Z",
                                            "error": {
                                                "message": "User by phone number: 89999999 not found"
                                            },
                                            "code": 404
                                        }
                                        """
                        )
                    }
            )
    )
    @GetMapping("/by-phone/{phone}")
    ResponseEntity<Users> getUserByPhone(
            @Parameter(description = "The phone number of the user we are looking for",
                    example = "89999999", required = true)
            @PathVariable String phone);



    @Operation(
            summary = "User by public id",
            description = "Getting user by public uuid",
            responses = @ApiResponse(responseCode = "200", description = "Success response",
                    content = @Content(schema = @Schema(implementation = Users.class))
            )
    )
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "User by phone number not found",
                                    value = """
                                        {
                                            "timestamp": "2024-07-15T14:30:45Z",
                                            "error": {
                                                "message": "User by public id: bc10cb59-8e55-477a-96d6-8485b56334ee not found"
                                            },
                                            "code": 404
                                        }
                                        """
                            )
                    }
            )
    )
    @GetMapping("/by-publicId/{publicId}")
    ResponseEntity<Users> getUserByPublicId(@PathVariable UUID publicId);


    @Operation(
            summary = "Update username",
            description = "Update username",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success request")
            }
    )
    @ApiResponse(responseCode = "400", description = "User with that name already exists",
            content = @Content(schema = @Schema(
                    implementation = ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "User by name already exists",
                                    value = """
                                               {
                                                    "timestamp": "2024-07-15T14:30:45Z",
                                                    "error": {
                                                        "message": "User by public id: bc10cb59-8e55-477a-96d6-8485b56334ee not found"
                                                    },
                                                    "code": 400
                                               }
                                            """
                            ),
                    }
            )
    )
    @PatchMapping("/{newUsername}/username")
    ResponseEntity<Users> updateUsername(
            @Parameter(description = "New username for update", example = "newUsername", required = true)
            @PathVariable String newUsername,
            @RequestHeader(USER_PUBLIC_ID) UUID publicId);




    @Operation(
            summary = "Update phone number",
            description = "Update update phone number",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success request")
            }
    )
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                                name = "User by phone number not found",
                                value = """
                                        {
                                            "timestamp": "2024-07-15T14:30:45Z",
                                            "error": {
                                                "message": "User by public id: bc10cb59-8e55-477a-96d6-8485b56334ee not found"
                                            },
                                            "code": 404
                                        }
                                        """
                        )
                    }
            )
    )
    @PatchMapping("/{phone}/phone")
    ResponseEntity<Users> updatePhone(
            @Parameter(description = "New phone number for update", example = "89999999", required = true)
            @PathVariable String phone,
            @RequestHeader(USER_PUBLIC_ID) UUID publicId);




    @Operation(
            summary = "Update email",
            description = "Update email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success request")
            }
    )
    @ApiResponse(responseCode = "400", description = "User with that email already exists",
            content = @Content(schema = @Schema(
                    implementation = ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "User by email already exists",
                                    value = """
                                               {
                                                    "timestamp": "2024-07-15T14:30:45Z",
                                                    "error": {
                                                        "message": "User by public id: bc10cb59-8e55-477a-96d6-8485b56334ee not found"
                                                    },
                                                    "code": 400
                                               }
                                            """
                            )
                    }
            )
    )
    @PatchMapping("/{email}/email")
    ResponseEntity<Users> updateEmail(
            @Parameter(description = "Email for update", example = "user@email.com")
            @PathVariable String email,
            @RequestHeader(USER_PUBLIC_ID) UUID publicId);


    @Operation(
            summary = "User assigned",
            description = "User assigned",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success request")
            }
    )
    @ApiResponse(responseCode = "404",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                name = "User by email not found",
                                value = """
                                        {
                                            "timestamp": "2024-07-15T14:30:45Z",
                                            "error": {
                                                "message": "User by email: user@email.com not found"
                                            },
                                            "code": 404
                                        }
                                        """
                            ),
                            @ExampleObject(
                                    name = "Hotel not found",
                                    value = """
                                            {
                                                "timestamp": "2024-07-15T14:30:45Z",
                                                "error": {
                                                        "message": "Hotel by id: 1 not found"
                                                },
                                                "code": 404
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Duplicate role",
                                    value = """
                                            {
                                                "timestamp": "2024-07-15T14:30:45Z",
                                                "error": {
                                                    "message": "Role: ROLE_NAMAGER the user already has it"
                                                },
                                                "code": 400
                                            }
                                            """
                            )
                    }
            )
    )
    @PatchMapping("/assign-hotel")
    ResponseEntity<Users> appointmentHotel(
            @Valid @RequestBody AppointmentRequestDto appointmentRequestDto);

}
