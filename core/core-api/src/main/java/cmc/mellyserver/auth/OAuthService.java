package cmc.mellyserver.auth;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.auth.controller.dto.request.OAuthSignupRequestDto;
import cmc.mellyserver.auth.controller.dto.response.OAuthResponseDto;
import cmc.mellyserver.auth.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.auth.dto.response.TokenResponseDto;
import cmc.mellyserver.auth.token.TokenDto;
import cmc.mellyserver.auth.token.TokenService;
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

	private final TokenService tokenService;

	private final FcmTokenRepository fcmTokenRepository;

	@Transactional
	public TokenResponseDto signup(OAuthSignupRequestDto oAuthSignupRequestDto) {

		User user = userWriter.save(oAuthSignupRequestDto.toEntity());
		TokenDto tokenDto = tokenService.createToken(user);
		fcmTokenRepository.saveToken(user.getId().toString(), oAuthSignupRequestDto.getFcmToken());
		return TokenResponseDto.of(tokenDto.accessToken(), tokenDto.refreshToken().getToken());
	}

	@Transactional
	public OAuthResponseDto login(OAuthLoginRequestDto oAuthLoginRequestDto) {

		LoginClient loginClient = loginClientFactory.find(oAuthLoginRequestDto.getProvider());
		LoginClientResult socialUser = loginClient.getUserData(oAuthLoginRequestDto.getAccessToken());
		User user = userReader.findBySocialId(socialUser.uid());

		if (Objects.isNull(user)) {
			return OAuthResponseDto.newUser(socialUser.uid(), socialUser.provider());
		}

		TokenDto tokenDto = tokenService.createToken(user);
		fcmTokenRepository.saveToken(user.getId().toString(), oAuthLoginRequestDto.getFcmToken());
		return new OAuthResponseDto(TokenResponseDto.of(tokenDto.accessToken(), tokenDto.refreshToken().getToken()),
			null);
	}

}
