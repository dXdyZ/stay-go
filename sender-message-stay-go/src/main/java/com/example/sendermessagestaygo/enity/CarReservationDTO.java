package com.example.sendermessagestaygo.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarReservationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String carName;
    private String reservationDate;
    private String dueDate;
    private Date careteDate;
    private String price;
    private String username;
    private String email;
    private String country;
    private String city;
    private String street;
    private String houseNumber;

}
