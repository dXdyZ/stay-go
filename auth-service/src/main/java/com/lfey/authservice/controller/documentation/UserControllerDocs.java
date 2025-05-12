package com.lfey.authservice.controller.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import com.lfey.authservice.controller.GlobalExceptionHandler;
import com.lfey.authservice.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;


@OpenAPIDefinition
public interface UserControllerDocs {

    String USERNAME_HEADER = "X-User-Username";

    @Operation(
            summary = "Brief user",
            description = "Getting username and roles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success request")
            }
    )
    @ApiResponse(responseCode = "404", description = "User by name not found",
            content = @Content(schema = @Schema(
                    implementation = GlobalExceptionHandler.ErrorResponse.class),
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
    @GetMapping("/{username}")
    ResponseEntity<UserBriefDto> getBriefUserByUsername(
            @Parameter(
                    description = "Unique user name (login)",
                    example = "user",
                    required = true
            )
            @PathVariable String username);



    @Operation(
            summary = "Update username",
            description = "Update username",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = UsernameUpdateDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success request")
            }
    )
    @ApiResponse(responseCode = "404", description = "User with that name already exists",
            content = @Content(schema = @Schema(
                    implementation = GlobalExceptionHandler.ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "User by name already exists",
                                    value = """
                                               {
                                                    "timestamp": "2024-07-15T14:30:45Z",
                                                    "error": {
                                                        "message": "User with name: newUsername already exists"
                                                    },
                                                    "code": 400
                                               }
                                            """
                            )
                    }
            )
    )
    @PatchMapping("/username")
    ResponseEntity<UserDetailsDto> updateUsername(@Valid @RequestBody UsernameUpdateDto usernameUpdateDto,
                                                         @Parameter(
                                                                 description = "The user's name obtained from the token",
                                                                 example = "oldUsername",
                                                                 required = true
                                                         )
                                                         @RequestHeader(USERNAME_HEADER) String username);


    @Operation(
            summary = "Update email",
            description = "Update email",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = EmailUpdateDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success request")
            }
    )
    @ApiResponse(responseCode = "400", description = "User with that email already exists",
            content = @Content(schema = @Schema(
                    implementation = GlobalExceptionHandler.ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "User by email already",
                                    value = """
                                               {
                                                    "timestamp": "2024-07-15T14:30:45Z",
                                                    "error": {
                                                        "message": "User with email: newUsername already exists"
                                                    },
                                                    "code": 400
                                               }
                                            """
                            )
                    }
            )
    )
    @PatchMapping("/email")
    void updateEmail(@Valid @RequestBody EmailUpdateDto emailUpdateDto,
                            @RequestHeader(USERNAME_HEADER) String username);



    @Operation(
        summary = "Update password",
        description = "Update password",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(schema = @Schema(implementation = ResetPasswordRequestDto.class))
        ),
        responses = {
                @ApiResponse(responseCode = "200", description = "Success request")
        }
    )
    @PatchMapping("/password")
    void updatePassword(@Valid @RequestBody ResetPasswordRequestDto passwordRequest,
                               @Parameter(
                                       description = "The name of the user whose password is being changed",
                                       example = "user",
                                       required = true
                               )
                               @RequestHeader(USERNAME_HEADER) String username);





    @Operation(
        summary = "Add role",
        description = "Add a role to a user",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(schema = @Schema(implementation = RoleRequestDto.class))
        ),
        responses = {
                @ApiResponse(responseCode = "200", description = "Success request")
        }
    )
    @ApiResponse(responseCode = "400", description = "Duplicate role",
            content = @Content(schema = @Schema(
                    implementation = GlobalExceptionHandler.ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "Duplicate role",
                                    value = """
                                               {
                                                    "timestamp": "2024-07-15T14:30:45Z",
                                                    "error": {
                                                        "message": "Role: ROLE_USER is already assigned to the user"
                                                    },
                                                    "code": 400
                                               }
                                            """
                            )
                    }
            )
    )
    @PatchMapping("/{username}/roles")
    void addRole(
            @Parameter(
                    description = "The name of the user who is assigned the role",
                    example = "user",
                    required = true
            )
            @PathVariable String username,
            @RequestBody RoleRequestDto addRoleRequestDto);


    @Operation(
            summary = "Delete role",
            description = "Delete a role from a specified user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = RoleRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success request")
            }
    )
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(
                    implementation = GlobalExceptionHandler.ErrorResponse.class),
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
    @DeleteMapping("/{username}/roles")
    void deleteRole(
            @Parameter(
                    description = "The name of the user to whom this role is being deleted",
                    example = "user",
                    required = true
            )
            @PathVariable String username,
            @RequestBody RoleRequestDto roleRequestDto);
}
