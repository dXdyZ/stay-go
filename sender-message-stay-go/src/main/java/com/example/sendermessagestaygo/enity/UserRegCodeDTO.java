package com.example.sendermessagestaygo.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegCodeDTO {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String email;
}
