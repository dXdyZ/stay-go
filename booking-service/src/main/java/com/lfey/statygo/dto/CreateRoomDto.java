package com.lfey.statygo.dto;

import com.lfey.statygo.entity.BedType;
import com.lfey.statygo.entity.RoomType;
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
public class CreateRoomDto {

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

    @NotNull(message = "The field must not be empty, choose one of the suggested options: " +
            "STANDARD, LUX, BUSINESS, PRESIDENT")
    private RoomType roomType;

    @NotNull(message = "Auto approve must be enabled or disabled.")
    private Boolean autoApprove;

    @NotNull(message = "The field must not be empty, choose one of the suggested options: " +
            "SINGLE, DOUBLE, QUEEN, KING, TWO_TIRE")
    private BedType bedType;
}
