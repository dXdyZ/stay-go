package com.staygo.enity.DTO.message_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogLastMessageDTO {
    private String name;
    private String message;
}
