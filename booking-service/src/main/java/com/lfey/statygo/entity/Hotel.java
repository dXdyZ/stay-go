package com.lfey.statygo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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

    private Boolean grade;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "room_id")
    private List<Room> room;
}
