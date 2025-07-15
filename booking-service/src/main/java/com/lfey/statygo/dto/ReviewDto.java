package com.lfey.statygo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewDto (
        @NotNull
        Long hotelId,
        String reviewDescription,
        @Min(value = 1, message = "The score should be in the range from 1 to 5")
        @Max(value = 5, message = "The score should be in the range from 1 to 5")
        @NotNull(message = "The assessment must be transmitted")
        Integer grade
){}
