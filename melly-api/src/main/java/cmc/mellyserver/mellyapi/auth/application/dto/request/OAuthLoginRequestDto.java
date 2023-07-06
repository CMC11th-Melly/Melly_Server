package cmc.mellyserver.mellyapi.auth.application.dto.request;

import cmc.mellyserver.mellycommon.enums.Provider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLoginRequestDto {

    private String accessToken;

    private Provider provider;

    private String fcmToken;

    @Builder
    public OAuthLoginRequestDto(String accessToken, Provider provider, String fcmToken) {
        this.accessToken = accessToken;
        this.provider = provider;
        this.fcmToken = fcmToken;
    }
}