package cmc.mellyserver.auth.application;

import cmc.mellyserver.auth.client.KakaoClient;
import cmc.mellyserver.auth.presentation.dto.AuthRequest;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final KakaoClient kakaoClient;
    @Transactional
    public void login(AuthRequest authRequest) {
        User kakaoUser = kakaoClient.getUserData(authRequest.getAccessToken());
        String socialId = kakaoUser.getSocialId();
        Optional<User> memberBySocialId = userRepository.findUserBySocialId(socialId);
    }
}
