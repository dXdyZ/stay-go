package com.example.sendermessagestaygo.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArmoredRoomDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
    private Date createDate;
    private String roomNumber;
    private String prestige;
    private String price;
    private String dateArmored;
    private String departureDate;
    private String hotelName;
    private String city;
    private String country;
    private String street;
    private String houseNumber;
}
