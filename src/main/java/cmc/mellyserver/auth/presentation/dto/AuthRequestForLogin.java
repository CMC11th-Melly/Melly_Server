package cmc.mellyserver.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestForLogin {

    @Schema(example = "melly@gmail.com")
    private String email;
    @Schema(example = "cmc11th")
    private String password;
}
