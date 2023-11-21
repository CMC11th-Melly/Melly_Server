package cmc.mellyserver.auth.dto.request;

import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.Builder;

public record OAuthLoginRequestDto(String accessToken, Provider provider, String fcmToken) {

    @Builder
    public OAuthLoginRequestDto {
    }

}