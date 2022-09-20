package cmc.mellyserver.auth.application;

import cmc.mellyserver.auth.client.KakaoClient;
import cmc.mellyserver.auth.client.NaverClient;
import cmc.mellyserver.auth.presentation.dto.AuthRequest;
import cmc.mellyserver.auth.token.AuthToken;
import cmc.mellyserver.auth.token.JwtTokenProvider;
import cmc.mellyserver.user.domain.RoleType;
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
    private final NaverClient naverClient;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public OAuthLoginResponseDto login(AuthRequest authRequest) {

        User socialUser;
        switch (authRequest.getProvider()){

            case KAKAO:
                    socialUser = kakaoClient.getUserData(authRequest.getAccessToken());
                    break;
            case NAVER:
                    socialUser = naverClient.getUserData(authRequest.getAccessToken());
                    break;
                default:
                    socialUser = null;
                    break;


        }

        String accessToken = getToken(socialUser.getUserId());

        Optional<User> user = userRepository.findUserByUserId(socialUser.getUserId());
        // 이미 회원가입한 유저라면?
        if(user.isPresent())
        {
            return new OAuthLoginResponseDto(accessToken,false,user.get());
        }

        // 아직 회원가입 하지 않았다면? 새로운 유저니깐 저장부터 해야함
      //  User findUser = User.builder().userId(socialUser.getUserId()).build();
        userRepository.save(socialUser);

        return new OAuthLoginResponseDto(accessToken,true, socialUser);

    }

    private String getToken(String userId)
    {
        AuthToken accessToken = jwtTokenProvider.createToken(userId, RoleType.USER, "100000000");
        return accessToken.getToken();
    }
}
