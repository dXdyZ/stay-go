package com.staygo.enity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(
            min = 4,
            max = 255,
            message = "Требуется улица, максимум 255 символов"
    )
    private String street;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Требуетяс название города, максимум 255 символов"
    )
    private String city;
    @NotNull
    @Size (
            min = 2,
            max = 20,
            message = "Требуется зип код, максимум 20 символов"
    )
    private String zipCode;
    @NotNull
    @Size (
            min = 2,
            max = 200,
            message = "Требуется название страны, максимум 200 символов"
    )
    private String country;

}
