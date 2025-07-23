package com.lfey.statygo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(
        description = "Review dto entity",
        example = """
                {
                    "hotelId": 1,
                    "reviewDescription": "There should be a review here",
                    "grade": 5
                }
                """
)
public record ReviewDto (
        @NotNull
        @Schema(description = "The ID of the hotel that the review is being created for", example = "1")
        Long hotelId,
        @Schema(description = "Review text", example = "There should be a review here")
        String reviewDescription,
        @Min(value = 1, message = "The score should be in the range from 1 to 5")
        @Max(value = 5, message = "The score should be in the range from 1 to 5")
        @NotNull(message = "The assessment must be transmitted")
        @Schema(description = "The hotel's rating by the user according to the 5-point system", example = "5")
        Integer grade
){}
