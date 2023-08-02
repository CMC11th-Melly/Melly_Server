package cmc.mellyserver.mellyapi.auth.application;

import cmc.mellyserver.mellyapi.auth.application.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.RefreshTokenDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.TokenResponseDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.OAuthSignupRequestDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.response.OAuthResponseDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.response.OAuthSignupResponseDto;
import cmc.mellyserver.mellyapi.auth.repository.RefreshToken;
import cmc.mellyserver.mellyapi.auth.repository.RefreshTokenRepository;
import cmc.mellyserver.mellyapi.common.token.JwtTokenProvider;
import cmc.mellyserver.mellycore.comment.application.event.SignupEvent;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import cmc.mellyserver.mellyinfra.client.LoginClient;
import cmc.mellyserver.mellyinfra.client.LoginClientFactory;
import cmc.mellyserver.mellyinfra.message.FCMService;
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

    private final RefreshTokenRepository refreshTokenRepository;

    private final FCMService fcmService;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public OAuthResponseDto login(OAuthLoginRequestDto oAuthLoginRequestDto) {

        LoginClient loginClient = loginClientFactory.find(oAuthLoginRequestDto.getProvider());
        User socialUser = loginClient.getUserData(oAuthLoginRequestDto.getAccessToken());

        // socialId를 이용해서 기존에 회원가입한 회원인지 체크한다.
        // 이메일의 경우, KAKAO는 비즈앱에 심사 거쳐야만 이메일을 필수로 받을 수 있다. 따라서 이메일이 무조건 있다는 보장 없음
        User user = userRepository.findUserBySocialId(socialUser.getSocialId());

        // 만약
        if (Objects.isNull(user)) {

            return new OAuthResponseDto(null, new OAuthSignupResponseDto(socialUser.getEmail(), socialUser.getSocialId(), socialUser.getProvider()));
        }

        user.updateOauthInfo(socialUser.getNickname(), socialUser.getSocialId());

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
        RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());

        //레디스에 저장 Refresh 토큰을 저장한다. (사용자 Id, refresh 토큰)
        refreshTokenRepository.save(new RefreshToken(refreshToken.getToken(), user.getId()), refreshToken.getExpiredAt());

        // 사용자 FCM 토큰을 Redis에 저장한다. -> 이벤트 처리 생각중
        fcmService.saveToken(user.getId(), oAuthLoginRequestDto.getFcmToken());

        return new OAuthResponseDto(TokenResponseDto.of(accessToken, refreshToken.getToken()), null);
    }


    public TokenResponseDto signup(OAuthSignupRequestDto oAuthSignupRequestDto) {

        User oauthLoginUser = User.createOauthLoginUser(oAuthSignupRequestDto.getSocialId(), oAuthSignupRequestDto.getProvider(), oAuthSignupRequestDto.getEmail(), oAuthSignupRequestDto.getNickname(), oAuthSignupRequestDto.getAgeGroup(), oAuthSignupRequestDto.getGender());
        User user = userRepository.save(oauthLoginUser);

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
        RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());

        //레디스에 저장 Refresh 토큰을 저장한다. (사용자 Id, refresh 토큰)
        refreshTokenRepository.save(new RefreshToken(refreshToken.getToken(), user.getId()), refreshToken.getExpiredAt());

        // 사용자 FCM 토큰을 Redis에 저장한다. -> 이벤트 처리 생각중
        fcmService.saveToken(user.getId(), oAuthSignupRequestDto.getFcmToken());

        if (Objects.nonNull(user.getEmail())) {
            publisher.publishEvent(new SignupEvent(user.getId()));
        }
        return TokenResponseDto.of(accessToken, refreshToken.getToken());
    }
}
