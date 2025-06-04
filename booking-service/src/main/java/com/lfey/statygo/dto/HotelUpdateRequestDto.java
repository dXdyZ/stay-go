package com.lfey.statygo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Entity for update hotel data",
        example = """
                {
                    "name": "New hotel name",
                    "stars": 4,
                    "description": "New data for updating information about the hotel"
                }
                """
)
public class HotelUpdateRequestDto {

    @NotBlank(message = "Name cannot be empty")
    @Schema(description = "New name", example = "New hotel name")
    private String name;

    @Min(value = 1, message = "Stars must be between 1 and 5")
    @Max(value = 5, message = "Stars must be between 1 and 5")
    @Schema(description = "New number of stars", example = "4")
    private Integer stars;

    @Schema(description = "New description", example = "New data for updating information about the hotel")
    private String description;
}
