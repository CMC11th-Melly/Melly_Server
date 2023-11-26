package cmc.mellyserver.auth.controller.dto.request;

import cmc.mellyserver.auth.service.dto.request.AuthLoginRequestDto;
import jakarta.validation.constraints.Email;

public record AuthLoginRequest(
    @Email(message = "이메일 형식을 지켜야 합니다")
    String email,

    String password,

    String fcmToken
) {

    public AuthLoginRequestDto toDto() {
        return new AuthLoginRequestDto(email, password, fcmToken);
    }

}