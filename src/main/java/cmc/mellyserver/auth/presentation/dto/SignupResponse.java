package cmc.mellyserver.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {

    @Schema(example = "melly@gmail.com")
    private String email;
}
