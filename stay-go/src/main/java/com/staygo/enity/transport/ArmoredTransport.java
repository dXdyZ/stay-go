package com.staygo.enity.transport;

import com.staygo.enity.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArmoredTransport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String armoredDate;

    private String endDateArmored;

    @ManyToOne
    private Users users;

    @ManyToOne
    private Transport transport;
}
