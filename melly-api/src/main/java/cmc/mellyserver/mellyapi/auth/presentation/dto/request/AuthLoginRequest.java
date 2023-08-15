package cmc.mellyserver.mellyapi.auth.presentation.dto.request;

import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthLoginRequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class AuthLoginRequest {

    @Email(message = "이메일 형식을 지켜야 합니다")
    private String email;

    private String password;

    private String fcmToken;

    public AuthLoginRequestDto toDto() {
        return new AuthLoginRequestDto(email, password, fcmToken);
    }

    public AuthLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}