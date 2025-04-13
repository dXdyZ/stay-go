package com.staygo.userservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

//TODO Остановился на обработке и возврате исключений
public record AppointmentRequest(
            @NotEmpty(message = "username must be not empty")
            @NotNull(message = "username must be not null")
            String username,
            @NotNull(message = "hotel id must be not null")
            @NumberFormat
            Long hotelId,
            @NotEmpty(message = "role name must be not empty")
            @NotNull(message = "role name must be not null")
            String roleName
) {}