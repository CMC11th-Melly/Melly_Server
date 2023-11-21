package cmc.mellyserver.auth.token;

import org.springframework.security.core.Authentication;

import cmc.mellyserver.auth.dto.response.RefreshTokenDto;
import cmc.mellyserver.dbcore.user.enums.RoleType;

public interface TokenProvider {

    String createAccessToken(Long userId, RoleType roleType);

    RefreshTokenDto createRefreshToken(Long userId, RoleType roleType);

    Authentication getAuthentication(String accessToken);

    long getLastExpiredTime(String accessToken);

    long extractUserId(String accessToken);

    boolean validateToken(String accessToken);

}