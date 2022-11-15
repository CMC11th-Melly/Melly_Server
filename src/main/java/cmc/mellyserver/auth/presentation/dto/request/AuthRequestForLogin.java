package cmc.mellyserver.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestForLogin {

    @Schema(example = "melly@gmail.com")
    private String email;
    @Schema(example = "asdfasdf")
    private String password;
    @Schema(description = "인증 유저 디바이스 fcm token")
    private String fcmToken;

    @Builder
    public AuthRequestForLogin(String email,String password,String fcmToken)
    {
        this.email = email;
        this.password = password;
        this.fcmToken = fcmToken;
    }
}
