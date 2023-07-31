package cmc.mellyserver.mellyapi.auth.presentation.dto.request;

import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthLoginRequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthLoginRequest {

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