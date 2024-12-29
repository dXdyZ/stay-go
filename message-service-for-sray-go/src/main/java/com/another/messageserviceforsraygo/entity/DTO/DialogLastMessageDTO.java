package com.another.messageserviceforsraygo.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogLastMessageDTO {
    private String name;
    private String message;
    private Date timestamp;
}
