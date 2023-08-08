package cmc.mellyserver.mellyapi.common.token;

import cmc.mellyserver.mellyapi.auth.application.dto.response.RefreshTokenDto;
import cmc.mellyserver.mellycore.common.enums.RoleType;
import org.springframework.security.core.Authentication;

public interface TokenProvider {

    String createAccessToken(final Long userId, final RoleType roleType);

    RefreshTokenDto createRefreshToken(final Long userId, final RoleType roleType);

    Authentication getAuthentication(final String accessToken);

    Long getLastExpireTime(final String accessToken);

    Long extractUserId(final String accessToken);

    boolean validateToken(final String accessToken);
}