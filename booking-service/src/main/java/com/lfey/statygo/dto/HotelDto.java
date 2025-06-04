package com.lfey.statygo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Hotel dto entity",
        example = """
                {
                    "hotelId": 1,
                    "grade": 4.5,
                    "stars": 5,
                    "name": "Hotel documentation",
                    "description": "This description of the hotel was created for documentation purposes",
                    "address": "12345, Russian, Moscow, Pushkina, 43a",
                    "totalPrice": 45450.213
                    "roomDto": [
                        {
                            "roomType": "STANDARD",
                            "capacity": 4,
                            "totalPrice": 1234.5,
                            "bedType": "DOUBLE",
                            "roomSize": 34.3
                        }
                    ],
                    "photoDto": [
                        {
                            "id": 1,
                            "url": "https://domain.com/photo/qwe123asdsda_filename.jpg",
                            "isMain": true
                        }
                    ]
                }
                """
)
public class HotelDto implements Serializable {
    @Schema(description = "Id of the requested hotel", example = "1")
    private Long hotelId;
    @Schema(description = "Average hotel rating (0.0 - 5.0)", example = "4.5")
    private Double grade;
    @Schema(description = "Number of stars (1 - 5)", example = "5")
    private Integer stars;
    @Schema(description = "Hotel name", example = "Hotel documentation")
    private String name;
    @Schema(description = "Description hotel",
            example = "This description of the hotel was created for documentation purposes")
    private String description;
    @Schema(description = "The address of the hotel is shown in the view for human viewing",
            example = "12345, Russian, Moscow, Pushkina, 43a")
    private String address;
    @Schema(description = "Total price", example = "45450.213")
    private Double totalPrice;

    @Builder.Default
    @Schema(description = "List of unique rooms in the hotel",
            example = """
                    {
                        "roomType": "STANDARD",
                        "capacity": 4,
                        "totalPrice": 1234.5,
                        "bedType": "DOUBLE",
                        "roomSize": 34.3
                    }
                    """)
    private List<RoomDto> roomDto = new ArrayList<>();

    @Builder.Default
    @Schema(description = "List of hotel image",
            example = """
                    {
                        "id": 1,
                        "url": "https://domain.com/photo/qwe123asdsda_filename.jpg",
                        "isMain": true
                    }
                    """)
    private List<PhotoDto> photoDto = new ArrayList<>();
}











