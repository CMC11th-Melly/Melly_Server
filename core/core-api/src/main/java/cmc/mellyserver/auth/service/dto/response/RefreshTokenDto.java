package cmc.mellyserver.auth.service.dto.response;

public record RefreshTokenDto(String token, long expiredAt) {
}
