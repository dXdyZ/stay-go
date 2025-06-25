package com.lfey.statygo.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Room dto entity",
        example = """
                {
                    "roomType": "STANDARD",
                    "capacity": 4,
                    "totalPrice": 1234.5,
                    "bedType": "DOUBLE",
                    "roomSize": 34.3,
                    "description": "Standard Double Room with 1 Bed or 2 Single Beds"
                }
                """
)
public class RoomDto {
    @Schema(description = "Room type", example = "STANDARD")
    private String roomType;
    @Schema(description = "Room capacity", example = "4")
    private Integer capacity;
    @Schema(description = "Price for the entire period of stay", example = "1234.5")
    private Double totalPrice;
    @Schema(description = "Bed type in the room", example = "DOUBLE")
    private String bedType;
    @Schema(description = "Room size", example = "34.3")
    private Double roomSize;
    @Schema(description = "Description room", example = "Standard Double Room with 1 Bed or 2 Single Beds")
    private String description;
}








