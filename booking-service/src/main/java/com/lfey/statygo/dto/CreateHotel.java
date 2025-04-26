package com.lfey.statygo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateHotel {

    @NotNull(message = "Hotel name cannot be null.")
    @NotEmpty(message = "Hotel name is required.")
    private String name;

    @NotNull(message = "Hotel stars cannot be null.")
    @NotEmpty(message = "Hotel stars are required.")
    private Integer stars;

    @NotNull(message = "Country cannot be null.")
    @NotEmpty(message = "Country is required.")
    private String country;

    @NotNull(message = "City cannot be null.")
    @NotEmpty(message = "City is required.")
    private String city;

    @NotNull(message = "Street cannot be null.")
    @NotEmpty(message = "Street is required.")
    private String street;

    @NotNull(message = "House number cannot be null.")
    @NotEmpty(message = "House number is required.")
    private String houseNumber;

    private String description;

    @NotNull(message = "Postal code cannot be null.")
    @NotEmpty(message = "Postal code is required.")
    private Long postalCode;
}
