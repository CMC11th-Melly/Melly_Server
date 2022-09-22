package cmc.mellyserver.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequest {

    @Schema(description = "리소스 서버 접근용 액세스 토큰")
    private String accessToken;

    @Schema(description = "소셜 로그인 서비스 제공자",example = "KAKAO")
    private Provider provider;
}
