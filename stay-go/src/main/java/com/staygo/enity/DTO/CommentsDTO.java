package com.staygo.enity.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDTO implements Serializable {
    private String comment;
    private Date createDate;
    private String username;
    private String hotelName;
}
