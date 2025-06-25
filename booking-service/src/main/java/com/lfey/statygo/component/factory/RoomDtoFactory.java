package com.lfey.statygo.component.factory;

import com.lfey.statygo.component.CustomDateFormatter;
import com.lfey.statygo.component.PriceCalculate;
import com.lfey.statygo.dto.RoomDto;
import com.lfey.statygo.entity.Room;
import org.springframework.stereotype.Component;

public class RoomDtoFactory {
    public static RoomDto createRoomDto(Room room, String startDate, String endDate) {
        return RoomDto.builder()
                .roomType(room.getRoomType().name())
                .capacity(room.getCapacity())
                .totalPrice(PriceCalculate.calculationTotalPrice(
                        room.getPricePerDay(),
                        CustomDateFormatter.localDateFormatter(startDate),
                        CustomDateFormatter.localDateFormatter(endDate)
                ))
                .description(room.getDescription())
                .bedType(room.getBedType().name())
                .build();
    }
}
