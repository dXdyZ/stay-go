package com.lfey.statygo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "hotels")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer stars;

    private Double grade;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;

    private String description;

    @Builder.Default
    @ToString.Exclude //Исключает поле из toString()
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            mappedBy = "hotel",
            fetch = FetchType.LAZY)
    private List<Room> room = new ArrayList<>();


    @Builder.Default
    @ToString.Exclude
    @OneToMany(
            mappedBy = "hotel",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<Photo> photos = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private HotelType hotelType;

    public Optional<Photo> getMainPhoto() {
        return photos.stream().filter(Photo::getIsMain).findFirst();
    }
}
