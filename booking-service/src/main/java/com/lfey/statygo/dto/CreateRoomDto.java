package com.lfey.statygo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoom {

    @NotNull(message = "Room number cannot be null.")
    @Min(value = 1, message = "Room number must be greater than or equal to 1.")
    private Integer number;

    @NotNull(message = "Capacity cannot be null.")
    @Min(value = 1, message = "Capacity must be at least 1 person.")
    private Integer capacity;

    @NotNull(message = "Price per day cannot be null.")
    @DecimalMin(value = "0.01", message = "Price per day must be greater than 0.")
    private Double pricePerDay;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotBlank(message = "Room type is required.")
    private String roomType;
}
