package cmc.mellyserver.auth.dto.response;

public record OAuthResponseDto(TokenResponseDto tokenInfo, OAuthSignupResponseDto data) {

    public static OAuthResponseDto newUser(String socialId, String provider) {
        return new OAuthResponseDto(null, new OAuthSignupResponseDto(socialId, provider));
    }

    public static OAuthResponseDto of(String accessToken, String refreshToken) {
        return new OAuthResponseDto(TokenResponseDto.of(accessToken, refreshToken), null);
    }
}
