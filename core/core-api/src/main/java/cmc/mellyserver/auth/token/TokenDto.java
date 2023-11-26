package cmc.mellyserver.auth.token;

import cmc.mellyserver.auth.service.dto.response.RefreshTokenDto;

public record TokenDto(String accessToken, RefreshTokenDto refreshToken) {

}
