package cmc.mellyserver.mellyappexternalapi.auth.presentation.dto.request;

import cmc.mellyserver.mellydomain.common.enums.Provider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLoginRequest {

    private String accessToken;

    private Provider provider;

    private String fcmToken;

    @Builder
    public OAuthLoginRequest(String accessToken, Provider provider, String fcmToken) {
        this.accessToken = accessToken;
        this.provider = provider;
        this.fcmToken = fcmToken;
    }
}
