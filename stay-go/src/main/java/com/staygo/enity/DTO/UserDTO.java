package com.staygo.enity.DTO;

import com.staygo.enity.user.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private List<Payment> payments;
}
