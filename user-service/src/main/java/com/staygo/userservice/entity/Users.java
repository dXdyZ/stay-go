package com.staygo.userservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Full user data",
        example = """
                {
                    "id": 1,
                    "username": "user",
                    "email": "user@email.com",
                    "phoneNumber": "89999999",
                    "hotelId": 1
                }
                """
)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier user", example = "1")
    private Long id;

    @Column(name = "email")
    @Schema(description = "User email address", example = "user@email.com")
    private String email;

    @Column(name = "phone_number")
    @Schema(description = "User phone number", example = "899999999")
    private String phoneNumber;

    @Column(name = "username")
    @Schema(description = "Unique user name and login")
    private String username;

    @Column(name = "hotel_id")
    @Schema(description = "Hotel id where the user works")
    private Long hotelId;

    @Column(name = "public_id", nullable = false, updatable = false, unique = true)
    public UUID publicId;
}
