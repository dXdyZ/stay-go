package com.lfey.statygo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(
        description = "Entity for create hotel",
        example = """
                {
                    "name": "Test hotel",
                    "stars": 5,
                    "country": "Russian",
                    "city": "Moscow",
                    "street": "Pushkina",
                    "houseNumber": "43a",
                    "description": "This hotel create for documentation",
                    "postalCode": 12345
                }
                """
)
public class CreateHotelDto {

    @NotNull(message = "Hotel name cannot be null")
    @NotEmpty(message = "Hotel name is required")
    @Schema(description = "Hotel name", example = "Test hotel")
    private String name;

    @NotNull(message = "Hotel stars cannot be null")
    @Min(value = 1, message = "The score should be in the range from 1 to 5")
    @Max(value = 5, message = "The score should be in the range from 1 to 5")
    @Schema(description = "The number of stars at the hotel", example = "5")
    private Integer stars;

    @NotNull(message = "Country cannot be null")
    @NotEmpty(message = "Country is required")
    @Schema(description = "The country where the hotel is located", example = "Russian")
    private String country;

    @NotNull(message = "City cannot be null")
    @NotEmpty(message = "City is required")
    @Schema(description = "The city where the hotel is located", example = "Moscow")
    private String city;

    @NotNull(message = "Street cannot be null")
    @NotEmpty(message = "Street is required")
    @Schema(description = "The street where the hotel is located", example = "Pushkina")
    private String street;

    @NotNull(message = "House number cannot be null")
    @NotEmpty(message = "House number is required")
    @Schema(description = "The number of the building where the hotel is located", example = "43a")
    private String houseNumber;

    @Schema(description = "Description of the hotel", example = "This hotel create for documentation")
    private String description;

    @NotNull(message = "Postal code cannot be null")
    @Schema(description = "The postal code of the hotel", example = "12345")
    private Long postalCode;

    @Schema(description = "Hotel photo")
    private List<MultipartFile> photos;

    @Schema(description = "Main photo index", example = "1")
    private Integer mainPhotoIndex;
}
