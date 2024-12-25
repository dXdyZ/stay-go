package com.staygo.enity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO implements Serializable {
    private String roomName;
    private String roomStatus;
    private BigDecimal price;
    private String prestige;
}
