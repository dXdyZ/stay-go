package com.staygo.enity.hotel;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.staygo.enity.address.Address;
import com.staygo.enity.user.Users;
import com.staygo.repository.hotel_repo.HotelRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(
            min = 5,
            max = 255,
            message = "Имя отеля должно быть в адекватных приделах"
    )
    private String name;

    @Digits(integer = 5, fraction = 0, message = "Оценка должна быть от 1 до 5 ")
    private Integer grade;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Comments> comments;

    @ManyToOne
    private Users users;

    @ToString.Exclude
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    //Фиксит цеклическую зависимость. Которая выдает дахуя строк в ответе
    @JsonManagedReference  // Указывает на "главную" сторону связи
    private List<Room> rooms;

    @ToString.Exclude
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelData> hotelData;


    public void addedRooms(Room room) {
        rooms.add(room);
    }

    public void addedHotelData(HotelData hotelDataFile) {
        hotelData.add(hotelDataFile);
    }
}
