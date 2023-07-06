package cmc.mellyserver.mellyapi.auth.application.impl;

import cmc.mellyserver.mellyapi.auth.application.OAuthService;
import cmc.mellyserver.mellyapi.auth.application.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.OAuthLoginResponseDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.AuthRequestForOAuthSignup;
import cmc.mellyserver.mellyapi.auth.presentation.dto.response.AuthResponseForLogin;
import cmc.mellyserver.mellyapi.common.token.JwtTokenProvider;
import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycommon.enums.RoleType;
import cmc.mellyserver.mellycore.common.aws.FileUploader;
import cmc.mellyserver.mellycore.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import cmc.mellyserver.mellyinfra.client.LoginClient;
import cmc.mellyserver.mellyinfra.client.LoginClientFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    private final UserRepository userRepository;

    private final LoginClientFactory loginClientFactory;

    private final JwtTokenProvider tokenProvider;

    private final FileUploader fileUploader;

    @Override
    @Transactional
    public OAuthLoginResponseDto login(OAuthLoginRequestDto oAuthLoginRequestDto) {

        LoginClient loginClient = loginClientFactory.find(oAuthLoginRequestDto.getProvider());
        User socialUser = loginClient.getUserData(oAuthLoginRequestDto.getAccessToken());

        Optional<User> findUser = userRepository.findUserByUserId(socialUser.getUserId());

        // 기존 유저가 존재하면 로그인 성공
        if (findUser.isPresent()) {
            String accessToken = getToken(socialUser.getUserSeq(), socialUser.getRoleType());
            findUser.get().setFcmToken(socialUser.getFcmToken());
            return new OAuthLoginResponseDto(accessToken, false, findUser.get());
        }

        // 존재하지 않는다면 회원가입으로 이동
        socialUser.setFcmToken(oAuthLoginRequestDto.getFcmToken());
        return new OAuthLoginResponseDto(null, true, socialUser);
    }

    private String getToken(Long userSeq, RoleType role) {
        return tokenProvider.createToken(userSeq, role);
    }

    @Transactional
    public AuthResponseForLogin signup(AuthRequestForOAuthSignup authRequestForOAuthSignup) {

        // 일단 유저 찾고
        User user = userRepository.findUserByUserId(authRequestForOAuthSignup.getUid()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_USER);
        });

        user.updateUser(authRequestForOAuthSignup.getNickname(),
                authRequestForOAuthSignup.getGender(),
                fileUploader.getMultipartFileName(authRequestForOAuthSignup.getProfileImage()),
                authRequestForOAuthSignup.getAgeGroup(), true, true, true);
        // 회원가입 완료 됐으니깐 토큰 생성
        String token = getToken(user.getUserSeq(), user.getRoleType());
        // 반환 해주기
        return AuthAssembler.loginResponse(token, user);

    }

}
