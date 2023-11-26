package cmc.mellyserver.auth.service.dto.request;

import lombok.Builder;

public record AuthLoginRequestDto(String email, String password, String fcmToken) {

    @Builder
    public AuthLoginRequestDto {
    }

}
