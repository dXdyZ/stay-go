package com.staygo.enity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Size (
            min = 2,
            max = 40,
            message = "Требуется имя, максимум 40 символо"
    )
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    private Date createDate = new Date();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    public void addedPayment(Payment payment) {
        payments.add(payment);
    }
}
