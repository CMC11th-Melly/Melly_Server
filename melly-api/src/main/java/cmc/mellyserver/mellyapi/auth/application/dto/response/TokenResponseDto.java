package cmc.mellyserver.mellyapi.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponseDto {

    private String accessToken;

    private String refreshToken;

    public static TokenResponseDto of(String accessToken, String refreshToken) {
        return new TokenResponseDto(accessToken, refreshToken);
    }
}