package cmc.mellyserver.auth.controller.dto.response;

import cmc.mellyserver.auth.dto.response.TokenResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthResponseDto<T> {

    public TokenResponseDto tokenInfo;

    public T data;

    public static OAuthResponseDto newUser(String socialId, String provider) {
        return new OAuthResponseDto(null, new OAuthSignupResponseDto(socialId, provider));
    }

    public static OAuthResponseDto of(String accessToken, String refreshToken) {
        return new OAuthResponseDto(TokenResponseDto.of(accessToken, refreshToken), null);
    }
}
