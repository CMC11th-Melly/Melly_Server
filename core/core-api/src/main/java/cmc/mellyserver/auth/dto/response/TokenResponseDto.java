package cmc.mellyserver.auth.dto.response;

public record TokenResponseDto(String accessToken, String refreshToken) {

    public static TokenResponseDto of(String accessToken, String refreshToken) {
        return new TokenResponseDto(accessToken, refreshToken);
    }

}
