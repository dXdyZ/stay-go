package com.staygo.enity.hotel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String roomName;
    private String roomStatus;
    private BigDecimal price;
    private String prestige;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RoomData> roomData;

    @ToString.Exclude
    @ManyToOne
    //Фиксит цеклическую зависимость
    @JsonBackReference  // Указывает на "обратную" сторону связи
    private Hotel hotel;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ArmoredRoom> armoredRoom;

    public void addRoomData(RoomData roomDataFile) {
        if (this.roomData == null) {
            this.roomData = new ArrayList<>();
        }
        this.roomData.add(roomDataFile);
    }
}
