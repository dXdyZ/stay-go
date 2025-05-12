package com.staygo.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

//TODO Остановился на обработке и возврате исключений
@Schema(description = "The appointment of an employee to the hotel and their roles",
        example = """
                {
                    "username": "user",
                    "hotelId": 1,
                    "roleName": "ROLE_MANAGER"
                }
                """
)
public record AppointmentRequestDto(
            @NotEmpty(message = "Username must be not empty")
            @NotNull(message = "Username must be not null")
            @Schema(description = "Name of the user being assigned")
            String username,
            @NotNull(message = "Hotel id must be not null")
            @NumberFormat
            @Schema(description = "The hotel to be assigned to")
            Long hotelId,
            @NotEmpty(message = "Role name must be not empty")
            @NotNull(message = "Role name must be not null")
            @Schema(description = "Role to be assigned", example = "ROLE_MANAGER")
            String roleName
) {}