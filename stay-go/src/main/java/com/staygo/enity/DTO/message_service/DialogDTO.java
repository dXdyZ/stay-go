package com.staygo.enity.DTO.message_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogDTO {
    private List<UserMessageDTO> userMessageDTOS;
    private List<MessageDTO> messageDTOS;
}
