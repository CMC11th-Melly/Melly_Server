package cmc.mellyserver.auth.controller.dto.request;

import cmc.mellyserver.auth.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLoginRequest {

    private String accessToken; // 리소스 서버 접근용 액세스 토큰

    private Provider provider; // 제공자

    private String fcmToken; // FCM 토큰

    @Builder
    public OAuthLoginRequest(String accessToken, Provider provider, String fcmToken) {
        this.accessToken = accessToken;
        this.provider = provider;
        this.fcmToken = fcmToken;
    }

    public OAuthLoginRequestDto toDto() {
        return OAuthLoginRequestDto.builder()
                .accessToken(accessToken)
                .fcmToken(fcmToken)
                .provider(provider)
                .build();
    }
}
