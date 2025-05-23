package com.lfey.statygo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private Long postalCode;

    public String getHumanReadableAddress() {
        return String.format("%s, %s, %s, %s, %s",
                this.postalCode, this.country, this.city, this.street, this.houseNumber);
    }
}
