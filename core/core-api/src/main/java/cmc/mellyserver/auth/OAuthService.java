package cmc.mellyserver.auth;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.auth.controller.dto.request.OAuthSignupRequestDto;
import cmc.mellyserver.auth.controller.dto.response.OAuthResponseDto;
import cmc.mellyserver.auth.controller.dto.response.OAuthSignupResponseDto;
import cmc.mellyserver.auth.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.auth.dto.response.RefreshTokenDto;
import cmc.mellyserver.auth.dto.response.TokenResponseDto;
import cmc.mellyserver.auth.repository.AuthTokenRepository;
import cmc.mellyserver.auth.repository.RefreshToken;
import cmc.mellyserver.auth.token.JwtTokenProvider;
import cmc.mellyserver.clientauth.LoginClient;
import cmc.mellyserver.clientauth.api.LoginClientResult;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbredis.repository.FcmTokenRepository;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.domain.user.UserWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

	private final UserReader userReader;

	private final UserWriter userWriter;

	private final LoginClientFactory loginClientFactory;

	private final JwtTokenProvider tokenProvider;

	private final AuthTokenRepository tokenRepository;

	private final FcmTokenRepository fcmTokenRepository;

	@Transactional
	public OAuthResponseDto login(OAuthLoginRequestDto oAuthLoginRequestDto) {

		LoginClient loginClient = loginClientFactory.find(oAuthLoginRequestDto.getProvider());
		LoginClientResult socialUser = loginClient.getUserData(oAuthLoginRequestDto.getAccessToken());

		User user = userReader.findBySocialId(socialUser.uid());

		if (Objects.isNull(user)) {
			return new OAuthResponseDto(null, new OAuthSignupResponseDto(socialUser.uid(), socialUser.provider()));
		}

		String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
		RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());
		tokenRepository.saveRefreshToken(new RefreshToken(refreshToken.getToken(), user.getId()),
			refreshToken.getExpiredAt());

		storeFCMToken(user, oAuthLoginRequestDto.getFcmToken());

		return new OAuthResponseDto(TokenResponseDto.of(accessToken, refreshToken.getToken()), null);
	}

	public TokenResponseDto signup(OAuthSignupRequestDto oAuthSignupRequestDto) {

		User user = userWriter
			.save(User.oauthUser(oAuthSignupRequestDto.getSocialId(), oAuthSignupRequestDto.getProvider(),
				oAuthSignupRequestDto.getEmail(), oAuthSignupRequestDto.getNickname(),
				oAuthSignupRequestDto.getAgeGroup(), oAuthSignupRequestDto.getGender()));

		String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
		RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());
		tokenRepository.saveRefreshToken(new RefreshToken(refreshToken.getToken(), user.getId()),
			refreshToken.getExpiredAt());

		storeFCMToken(user, oAuthSignupRequestDto.getFcmToken());

		return TokenResponseDto.of(accessToken, refreshToken.getToken());
	}

	private void storeFCMToken(User user, String oAuthLoginRequestDto) {
		fcmTokenRepository.saveToken(user.getId().toString(), oAuthLoginRequestDto);
	}

}
