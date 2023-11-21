package cmc.mellyserver.auth.dto.response;

public record RefreshTokenDto(String token, long expiredAt) {
}
