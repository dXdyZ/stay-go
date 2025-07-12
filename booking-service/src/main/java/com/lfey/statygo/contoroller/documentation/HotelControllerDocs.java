package com.lfey.statygo.contoroller.documentation;

import com.lfey.statygo.component.PageResponse;
import com.lfey.statygo.contoroller.GlobalExceptionHandler;
import com.lfey.statygo.dto.CreateHotelDto;
import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.dto.HotelUpdateRequestDto;
import com.lfey.statygo.entity.Hotel;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@OpenAPIDefinition
public interface HotelControllerDocs {


    @Operation(
            summary = "Create hotel",
            description = "Create new hotel",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = CreateHotelDto.class))
            ),
            responses = @ApiResponse(responseCode = "201", description = "Create hotel")
    )
    @PostMapping("/create")
    void createHotel(@Valid @RequestBody CreateHotelDto createHotelDto);



    @Operation(
            summary = "Update hotel data",
            description = "Update hotel data",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = HotelUpdateRequestDto.class))
            ),
            responses = @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Hotel not found",
                                    value = """
                                            {
                                                "timestamp": "2024-07-15T14:30:45Z",
                                                "error": {
                                                    "message": "Hotel by id: 1 not found"
                                                },
                                                "code": 404
                                            }
                                            """
                            )
                    )
            )
    )
    @PatchMapping("/{id}")
    void updateHotelData(
            @Parameter(description = "Id of the hotel whose data is being updated", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody HotelUpdateRequestDto hotelUpdateRequest);



    @Operation(
            summary = "Get hotel by id",
            description = "Get a hotel by id with conversion for human viewing",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success response",
                            content = @Content(schema = @Schema(implementation = HotelDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Hotel not found",
                            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                                    examples = @ExampleObject(
                                            name = "Hotel not found",
                                            value = """
                                                    {
                                                        "timestamp": "2024-07-15T14:30:45Z",
                                                        "error": {
                                                            "message": "Hotel by id: 1 not found"
                                                        },
                                                        "code": 404
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/users/{id}/{guests}/{startDate}/{endDate}")
    ResponseEntity<HotelDto> getHotelByIdFindUser(
            @Parameter(description = "Hotel id", example = "1", required = true)
            @PathVariable Long id,
            @Schema(description = "Number of guest", example = "2")
            @PathVariable Integer guests,
            @Schema(description = "Check-in date", example = "2025-11-12")
            @PathVariable String startDate,
            @Schema(description = "Departure date", example = "2025-11-15")
            @PathVariable String endDate
    );


    @Operation(
            summary = "Get hotel by id",
            description = "Get a hotel by id for internal work",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success response",
                            content = @Content(schema = @Schema(implementation = Hotel.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Hotel not found",
                            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                                    examples = @ExampleObject(
                                            name = "Hotel not found",
                                            value = """
                                                    {
                                                        "timestamp": "2024-07-15T14:30:45Z",
                                                        "error": {
                                                            "message": "Hotel by id: 1 not found"
                                                        },
                                                        "code": 404
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<Hotel> getHotelById(
            @Parameter(description = "Hotel id", example = "1", required = true)
            @PathVariable Long id);


    @Operation(
            summary = "Delete hotel",
            description = "Delete hotel by id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success delete"),
                    @ApiResponse(responseCode = "404", description = "Hotel not found",
                            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                                    examples = @ExampleObject(
                                            name = "Hotel not found",
                                            value = """
                                                    {
                                                        "timestamp": "2024-07-15T14:30:45Z",
                                                        "error": {
                                                            "message": "Hotel by id: 1 not found"
                                                        },
                                                        "code": 404
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    void deleteHotelById(
            @Parameter(description = "Id of the hotel being deleted", example = "1", required = true)
            @PathVariable Long id);

    @Operation(
            summary = "Search hotel",
            description = "Search for hotels by filters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success response",
                            content = @Content(schema = @Schema(implementation = PageResponse.class),
                                    examples = @ExampleObject(
                                            name = "Hotel dto page response",
                                            value = """
                                                    {
                                                        "content": [
                                                            "hotelId": 1,
                                                            "grade": 4.5,
                                                            "stars": 5,
                                                            "name": "Hotel documentation",
                                                            "description": "This description of the hotel was created for documentation purposes",
                                                            "address": "12345, Russian, Moscow, Pushkina, 43a",
                                                            "totalPrice": 45555.01
                                                            "roomDto": [
                                                                {
                                                                    "roomType": "STANDARD",
                                                                    "capacity": 4,
                                                                    "totalPrice": 1234.5,
                                                                    "bedType": "DOUBLE",
                                                                    "roomSize": 34.3
                                                                }
                                                            ]
                                                        ],
                                                        "currentPage": 0,
                                                        "totalPages": 5,
                                                        "totalItems": 50
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/search")
    ResponseEntity<PageResponse<HotelDto>> searchHotels(
            @Parameter(description = "Check-in date", example = "2025-01-01")
            @RequestParam String startDate,
            @Parameter(description = "Departure date", example = "2025-01-02")
            @RequestParam String checkOut,
            @Parameter(description = "Number of stars at hotel (1 - 5)", example = "5")
            @RequestParam(required = false) Integer stars,
            @Parameter(description = "Country", example = "Russian")
            @RequestParam(required = false) String country,
            @Parameter(description = "City", example = "Moscow")
            @RequestParam(required = false) String city,
            @Parameter(description = "Users grade", example = "4.8")
            @RequestParam(required = false) Double grade,
            @Parameter(description = "Type of housing", example = "HOTEL")
            @RequestParam(required = false) String hotelType,
            @Parameter(description = "Minimal price for period", example = "10000.0")
            @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximal price for period", example = "100000.0")
            @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Search page Number", example = "0")
            @RequestParam(defaultValue = "0") int page
    );



    @Operation(
            summary = "Add photo",
            description = "Add photo or photos to the hotel",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success add photo"),
                    @ApiResponse(responseCode = "404", description = "Hotel not found",
                            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                                    examples = @ExampleObject(
                                            name = "Hotel not found",
                                            value = """
                                                    {
                                                        "timestamp": "2024-07-15T14:30:45Z",
                                                        "error": {
                                                            "message": "Hotel by id: 1 not found"
                                                        },
                                                        "code": 404
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping(value = "/{hotelId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void addPhoto(@PathVariable Long hotelId,
                  @RequestParam("mainPhoto") Integer mainPhoto,
                  @RequestParam("photos") List<MultipartFile> photos);
}
