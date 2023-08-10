package cmc.mellyserver.mellyapi.auth.application;

import cmc.mellyserver.mellyapi.auth.application.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.RefreshTokenDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.TokenResponseDto;
import cmc.mellyserver.mellyapi.auth.application.loginclient.LoginClient;
import cmc.mellyserver.mellyapi.auth.application.loginclient.LoginClientFactory;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.OAuthSignupRequestDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.response.OAuthResponseDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.response.OAuthSignupResponseDto;
import cmc.mellyserver.mellyapi.auth.repository.JWTRepository;
import cmc.mellyserver.mellyapi.auth.repository.RefreshToken;
import cmc.mellyserver.mellyapi.common.token.JwtTokenProvider;
import cmc.mellyserver.mellycore.comment.application.event.SignupEvent;
import cmc.mellyserver.mellycore.infrastructure.message.fcm.FCMTokenManageService;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
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

    private final FCMTokenManageService tokenManageService;

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
        tokenManageService.saveToken(user.getId(), oAuthLoginRequestDto.getFcmToken());

        return new OAuthResponseDto(TokenResponseDto.of(accessToken, refreshToken.getToken()), null);
    }


    public TokenResponseDto signup(OAuthSignupRequestDto oAuthSignupRequestDto) {

        User oauthLoginUser = User.createOauthLoginUser(oAuthSignupRequestDto.getSocialId(), oAuthSignupRequestDto.getProvider(), oAuthSignupRequestDto.getEmail(), oAuthSignupRequestDto.getNickname(), oAuthSignupRequestDto.getAgeGroup(), oAuthSignupRequestDto.getGender());
        User user = userRepository.save(oauthLoginUser);

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
        RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());

        tokenRepository.saveRefreshToken(new RefreshToken(refreshToken.getToken(), user.getId()), refreshToken.getExpiredAt());
        tokenManageService.deleteToken(user.getId());

        // 이메일을 받아왔다면 회원가입 축하 메일 전송
        if (Objects.nonNull(user.getEmail())) {
            publisher.publishEvent(new SignupEvent(user.getId()));
        }

        return TokenResponseDto.of(accessToken, refreshToken.getToken());
    }
}
