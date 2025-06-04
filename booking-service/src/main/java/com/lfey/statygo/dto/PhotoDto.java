package com.lfey.statygo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Photo for client",
        example = """
                    {
                        "id": 1,
                        "url": "https://domain.com/photo/qwe123asdsda_filename.jpg",
                        "isMain": true
                    }""")
public class PhotoDto implements Serializable {
    @Schema(description = "Photo id", example = "1")
    private Long id;
    @Schema(description = "Url for get photo", example = "https://domain.com/photo/qwe123asdsda_filename.jpg")
    private String url;
    @Schema(description = "The indicator is whether the photo is the main one", example = "false")
    private Boolean isMain;
}
