package cmc.mellyserver.common.token;

import cmc.mellyserver.dbcore.user.enums.RoleType;
import cmc.mellyserver.domain.auth.dto.response.RefreshTokenDto;
import org.springframework.security.core.Authentication;

public interface TokenProvider {

    String createAccessToken(final Long userId, final RoleType roleType);

    RefreshTokenDto createRefreshToken(final Long userId, final RoleType roleType);

    Authentication getAuthentication(final String accessToken);

    Long getLastExpireTime(final String accessToken);

    Long extractUserId(final String accessToken);

    boolean validateToken(final String accessToken);
}