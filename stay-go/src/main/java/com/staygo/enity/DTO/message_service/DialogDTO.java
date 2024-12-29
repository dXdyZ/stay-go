package com.staygo.enity.DTO.message_service;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("users")
    private List<UserMessageDTO> userMessageDTOS;
    @JsonProperty("messages")
    private List<MessageDTO> messageDTOS;
}
