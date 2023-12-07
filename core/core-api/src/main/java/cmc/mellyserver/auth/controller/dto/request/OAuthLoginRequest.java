package cmc.mellyserver.auth.controller.dto.request;

import cmc.mellyserver.auth.service.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.Builder;

public record OAuthLoginRequest(String accessToken, Provider provider, String fcmToken) {

    @Builder
    public OAuthLoginRequest {
    }

    public OAuthLoginRequestDto toDto() {
        return OAuthLoginRequestDto.builder().accessToken(accessToken).fcmToken(fcmToken).provider(provider).build();
    }
}
