package com.staygo.enity.hotel;

import com.staygo.enity.user.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Comments implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String comment;

    @Builder.Default
    private Date creatDate = new Date();

    @ManyToOne
    private Users users;

    @ManyToOne
    private Hotel hotel;

}
