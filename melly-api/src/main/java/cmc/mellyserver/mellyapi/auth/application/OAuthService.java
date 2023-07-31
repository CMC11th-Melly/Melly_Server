package cmc.mellyserver.mellyapi.auth.application;

import cmc.mellyserver.mellyapi.auth.application.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.mellyapi.common.token.JwtTokenProvider;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import cmc.mellyserver.mellyinfra.client.LoginClient;
import cmc.mellyserver.mellyinfra.client.LoginClientFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public String login(OAuthLoginRequestDto oAuthLoginRequestDto) {

        LoginClient loginClient = loginClientFactory.find(oAuthLoginRequestDto.getProvider()); // provider에 맞는 로그인 클라이언트 가져오기
        User socialUser = loginClient.getUserData(oAuthLoginRequestDto.getAccessToken()); // 유저 정보 받아오기

        User user = userRepository.findUserBySocialId(socialUser.getSocialId()); // 기존에 존재하는 유저가 있는지 찾는다.

        if (Objects.isNull(user)) { // 만약에 기존에 존재하는 유저가 없다면
            User savedUser = userRepository.save(socialUser); // 새로 만든 유저를 저장한다.
            return tokenProvider.createToken(savedUser.getId(), savedUser.getRoleType());
        }

        user.updateSocialLoginInfo(socialUser.getSocialId(), socialUser.getProvider());
        return tokenProvider.createToken(user.getId(), user.getRoleType());
    }


}
