package com.staygo.enity.hotel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RoomData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @Builder.Default
    private Date createDate = new Date();

    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonBackReference
    private Room room;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;
}
