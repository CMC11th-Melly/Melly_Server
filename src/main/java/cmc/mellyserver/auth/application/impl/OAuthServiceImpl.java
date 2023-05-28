package cmc.mellyserver.auth.application.impl;

import cmc.mellyserver.auth.application.LoginClient;
import cmc.mellyserver.auth.application.LoginClientFactory;
import cmc.mellyserver.auth.application.OAuthService;
import cmc.mellyserver.auth.application.dto.response.OAuthLoginResponseDto;
import cmc.mellyserver.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.auth.presentation.dto.request.AuthRequestForOAuthSignup;
import cmc.mellyserver.auth.presentation.dto.request.OAuthLoginRequest;
import cmc.mellyserver.auth.presentation.dto.response.AuthResponseForLogin;
import cmc.mellyserver.common.token.JwtTokenProvider;
import cmc.mellyserver.common.util.aws.FileUploader;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.enums.RoleType;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.repository.UserRepository;
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
    public OAuthLoginResponseDto login(OAuthLoginRequest oAuthLoginRequest) {

        LoginClient loginClient = loginClientFactory.find(oAuthLoginRequest.getProvider());
        User socialUser = loginClient.getUserData(oAuthLoginRequest.getAccessToken());

        Optional<User> findUser = userRepository.findUserByUserId(socialUser.getUserId());

        // 기존 유저가 존재하면 로그인 성공
        if(findUser.isPresent())
        {
            String accessToken = getToken(socialUser.getUserSeq(), socialUser.getRoleType());
            findUser.get().setFcmToken(socialUser.getFcmToken());
            return new OAuthLoginResponseDto(accessToken, false, findUser.get());
        }

        // 존재하지 않는다면 회원가입으로 이동
        socialUser.setFcmToken(oAuthLoginRequest.getFcmToken());
        return new OAuthLoginResponseDto(null,true, socialUser);
    }

    private String getToken(Long userSeq, RoleType role) {
        return tokenProvider.createToken(userSeq, role);
    }

    @Transactional
    public AuthResponseForLogin signup(AuthRequestForOAuthSignup authRequestForOAuthSignup) {

        // 일단 유저 찾고
        User user = userRepository.findUserByUserId(authRequestForOAuthSignup.getUid()).orElseThrow(()->{throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);});
        // 있으면 업데이트 하고
        user.updateUser(authRequestForOAuthSignup.getNickname(),authRequestForOAuthSignup.getGender(),fileUploader.getMultipartFileName(authRequestForOAuthSignup.getProfileImage()),authRequestForOAuthSignup.getAgeGroup(),true,true,true);
        // 회원가입 완료 됐으니깐 토큰 생성
        String token = getToken(user.getUserSeq(),user.getRoleType());
        // 반환 해주기
        return AuthAssembler.loginResponse(token, user);

    }

}
