package cmc.mellyserver.auth.presentation.dto.request;


import cmc.mellyserver.common.enums.Provider;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLoginRequest {

    private String accessToken;

    private Provider provider;

    private String fcmToken;
}
