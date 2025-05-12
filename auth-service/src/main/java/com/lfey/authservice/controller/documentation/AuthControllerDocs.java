package com.lfey.authservice.controller.documentation;

import com.lfey.authservice.controller.GlobalExceptionHandler;
import com.lfey.authservice.dto.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@OpenAPIDefinition
public interface AuthControllerDocs {

    String USERNAME_HEADER = "X-User-Username";

    @Operation(
        summary = "Registration user",
        description = "Register new user",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(schema = @Schema(implementation = UserRegistrationDto.class))
        ),
        responses = {
                @ApiResponse(responseCode = "200", description = "Success request")
        }
    )
    @ApiResponse(responseCode = "400", description = "Duplication of a user by name or email",
            content = @Content(schema = @Schema(
                    implementation = GlobalExceptionHandler.ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "Email already exists",
                                    value = """
                                               {
                                                    "timestamp": "2024-07-15T14:30:45Z",
                                                    "error": {
                                                        "message": "The user email: user@email.com already exists"
                                                    },
                                                    "code": 400
                                               }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Username already exists",
                                    value = """
                                           {
                                                "timestamp": "2024-07-15T14:30:45Z",
                                                "error": {
                                                    "message": "The user email: user already exists"
                                                },
                                                "code": 400
                                           }
                                           """
                            )
                    }
            )
    )
    @PostMapping("/register")
    void registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDTO);


    @Operation(
            summary = "Confirm registration",
            description = "Confirm registration with the help code",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ValidationCodeDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Code passed verification",
                            content = @Content(schema = @Schema(implementation = JwtTokenDto.class))
                    ),

            }
    )
    @ApiResponse(responseCode = "400", description = "Invalid code",
                            content = @Content(
                                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Invalid code",
                                                    value = """
                                                            {
                                                                 "timestamp": "2024-07-15T14:30:45Z",
                                                                 "error": {
                                                                        "message": "Invalid code"
                                                                 },
                                                                 "code": 400
                                                            }
                                                            """
                                            )
                                    }
                            )
    )
    @PostMapping("/registration/confirm")
    ResponseEntity<JwtTokenDto> validationUser(@Valid @RequestBody ValidationCodeDto validationCodeDto);

    @Operation(
            summary = "User login",
            description = "EndPoint for login user who registration",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = AuthRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Success login",
                            content = @Content(schema = @Schema(implementation = JwtTokenDto.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid username or password",
                            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                                    examples = {
                                        @ExampleObject(
                                                name = "Invalid username or password",
                                                value = """
                                                        {
                                                            "timestamp": "2024-07-15T14:30:45Z",
                                                            "error": {
                                                                "message": "Invalid username or password"
                                                            },
                                                            "code": 401
                                                        }
                                                        """
                                        )
                                    }
                            )
                    )
            }
    )
    @PostMapping("/login")
    ResponseEntity<JwtTokenDto> login(@Valid @RequestBody AuthRequestDto authRequestDto);

    @Operation(
            summary = "Refresh token",
            description = "EndPoint for refresh access token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = JwtTokenDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Update access token",
                            content = @Content(schema = @Schema(implementation = JwtTokenDto.class),
                                    examples = {
                                        @ExampleObject(
                                                name = "Return access token",
                                                value = """
                                                        {
                                                            "accessToken": "dsjhalvbdbu214vhhdvfal;dsjfau1"
                                                        }
                                                        """
                                        )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid refresh token",
                            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                                    examples = {
                                        @ExampleObject(
                                                name = "Invalid jwt token",
                                                value = """
                                                        {
                                                            "timestamp": "2024-07-15T14:30:45Z",
                                                            "error": {
                                                                "message": "Invalid or expired JWT token"
                                                            },
                                                            "code": 401
                                                        }
                                                        """
                                        ),
                                        @ExampleObject(
                                                name = "Token was revoked",
                                                value = """
                                                        {
                                                            "timestamp": "2024-07-15T14:30:45Z",
                                                            "error": {
                                                                "message": "Token was revoked"
                                                            },
                                                            "code": 401
                                                        }
                                                        """
                                        )
                                    }
                            )
                    )
            }
    )
    @PostMapping("/token/refresh")
    ResponseEntity<JwtTokenDto> refreshAccessToken(@Valid @RequestBody JwtTokenDto jwtTokenDto);

    @Operation(
            summary = "Confirm new email",
            description = "Confirm the updated email",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = JwtTokenDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Updated user data",
                            content = @Content(schema = @Schema(implementation = UserDetailsDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid code",
                            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                                    examples = {
                                        @ExampleObject(
                                                name = "Invalid code",
                                                value = """
                                                        {
                                                             "timestamp": "2024-07-15T14:30:45Z",
                                                             "error": {
                                                                    "message": "Invalid code"
                                                             },
                                                             "code": 400
                                                        }
                                                        """
                                        )
                                    }
                            )
                    )
            }

    )
    @PostMapping("/email-update/confirm")
    ResponseEntity<UserDetailsDto> validationUpdateEmail(@Valid @RequestBody ValidationCodeDto validationCodeDto,
                                                         @RequestHeader(USERNAME_HEADER) String username);
}
