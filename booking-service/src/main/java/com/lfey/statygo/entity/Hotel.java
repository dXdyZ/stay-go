package com.lfey.statygo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "hotels")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer stars;

    private Double grade;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    private String description;

    @Builder.Default
    @ToString.Exclude //Исключает поле из toString()
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> room = new ArrayList<>();

}
