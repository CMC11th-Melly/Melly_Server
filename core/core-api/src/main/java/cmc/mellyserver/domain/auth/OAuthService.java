package cmc.mellyserver.domain.auth;

import cmc.mellyserver.clientauth.LoginClient;
import cmc.mellyserver.common.token.JwtTokenProvider;
import cmc.mellyserver.controller.auth.dto.request.OAuthSignupRequestDto;
import cmc.mellyserver.controller.auth.dto.response.OAuthResponseDto;
import cmc.mellyserver.controller.auth.dto.response.OAuthSignupResponseDto;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.dbredis.repository.FcmTokenRepository;
import cmc.mellyserver.domain.auth.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.domain.auth.dto.response.RefreshTokenDto;
import cmc.mellyserver.domain.auth.dto.response.TokenResponseDto;
import cmc.mellyserver.domain.auth.repository.JWTRepository;
import cmc.mellyserver.domain.auth.repository.RefreshToken;
import cmc.mellyserver.domain.comment.event.SignupCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;

    private final LoginClientFactory loginClientFactory;

    private final JwtTokenProvider tokenProvider;

    private final JWTRepository tokenRepository;

    private final FcmTokenRepository fcmTokenRepository;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public OAuthResponseDto login(OAuthLoginRequestDto oAuthLoginRequestDto) {

        LoginClient loginClient = loginClientFactory.find(oAuthLoginRequestDto.getProvider());
        User socialUser = loginClient.getUserData(oAuthLoginRequestDto.getAccessToken());

        User user = userRepository.findUserBySocialId(socialUser.getSocialId());

        // 만약
        if (Objects.isNull(user)) {

            return new OAuthResponseDto(null, new OAuthSignupResponseDto(socialUser.getEmail(), socialUser.getSocialId(), socialUser.getProvider()));
        }

        user.updateOauthInfo(socialUser.getNickname(), socialUser.getSocialId());

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
        RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());

        tokenRepository.saveRefreshToken(new RefreshToken(refreshToken.getToken(), user.getId()), refreshToken.getExpiredAt());
        fcmTokenRepository.saveToken(user.getId().toString(), oAuthLoginRequestDto.getFcmToken());

        return new OAuthResponseDto(TokenResponseDto.of(accessToken, refreshToken.getToken()), null);
    }


    public TokenResponseDto signup(OAuthSignupRequestDto oAuthSignupRequestDto) {

        User oauthLoginUser = User.createOauthLoginUser(oAuthSignupRequestDto.getSocialId(), oAuthSignupRequestDto.getProvider(), oAuthSignupRequestDto.getEmail(), oAuthSignupRequestDto.getNickname(), oAuthSignupRequestDto.getAgeGroup(), oAuthSignupRequestDto.getGender());
        User user = userRepository.save(oauthLoginUser);

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
        RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());

        tokenRepository.saveRefreshToken(new RefreshToken(refreshToken.getToken(), user.getId()), refreshToken.getExpiredAt());
        fcmTokenRepository.deleteToken(user.getId().toString());

        // 이메일을 받아왔다면 회원가입 축하 메일 전송
        if (Objects.nonNull(user.getEmail())) {
            publisher.publishEvent(new SignupCompletedEvent(user.getId()));
        }

        return TokenResponseDto.of(accessToken, refreshToken.getToken());
    }
}
