package cmc.mellyserver.auth.token;

import org.springframework.security.core.Authentication;

import cmc.mellyserver.auth.dto.response.RefreshTokenDto;
import cmc.mellyserver.dbcore.user.enums.RoleType;

public interface TokenProvider {

  String createAccessToken(final Long userId, final RoleType roleType);

  RefreshTokenDto createRefreshToken(final Long userId, final RoleType roleType);

  Authentication getAuthentication(final String accessToken);

  Long getLastExpireTime(final String accessToken);

  Long extractUserId(final String accessToken);

  boolean validateToken(final String accessToken);

}