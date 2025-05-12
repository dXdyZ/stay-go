package com.lfey.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //Исключает пол с null в ответе
@Schema(
        description = "JWT Token",
        example = """
                {
                    "accessToken": "y12312312vv1231g23jh1hj23gh23h1h23gh12ghj312g12yu12l2ue;/i21ihexb1",
                    "refreshToken": "123jk2h3jb2b1.1.mnmdmnfsdfhss8dfsjdfjh"
                }
                """
)
public class JwtTokenDto {
    @NotBlank(message = "Access token must not be empty")
    @Schema(description = "Access token for authorization", example = "2312312hjh3j12h3j12h1g23g123g12g31gh31gh")
    private String accessToken;

    @NotBlank(message = "Refresh token must not be empty")
    @Schema(description = "Refresh token required for refresh access toke", example = "1231232131212h1hh1h12h2h3h3h3")
    private String refreshToken;
}
