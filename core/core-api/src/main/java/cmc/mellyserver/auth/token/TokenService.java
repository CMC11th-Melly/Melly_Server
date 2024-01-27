package cmc.mellyserver.auth.token;

import org.springframework.stereotype.Component;

import cmc.mellyserver.auth.service.dto.response.RefreshTokenDto;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;

    private final AuthTokenDao authTokenDao;

    public TokenDto createToken(User user) {
        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
        RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());
        authTokenDao.saveRefreshToken(new RefreshToken(refreshToken.token(), user.getId()),
            refreshToken.expiredAt());
        return new TokenDto(accessToken, refreshToken);
    }

    public RefreshToken findRefreshToken(Long userId) {
        return authTokenDao.findRefreshToken(userId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.RELOGIN_REQUIRED);
        });
    }

    public void makeAccessTokenDisabled(String accessToken) {
        long lastExpireTime = tokenProvider.getLastExpiredTime(accessToken);
        authTokenDao.makeAccessTokenDisabled(accessToken, lastExpireTime);
    }

    public void removeRefreshToken(Long userId) {
        authTokenDao.removeRefreshToken(userId);
    }

    public long extractUserId(String accessToken) {
        return tokenProvider.extractUserId(accessToken);
    }

}
