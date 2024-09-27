package com.staygo.enity.hotel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @Builder.Default
    private Date createDate = new Date();

    @ManyToOne
    private Hotel hotel;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;
}
