package cmc.mellyserver.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequest {

    private String accessToken;

    @Schema(example = "kakao")
    private Provider provider;
}
