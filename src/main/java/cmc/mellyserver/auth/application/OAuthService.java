package cmc.mellyserver.auth.application;

import cmc.mellyserver.auth.client.GoogleClient;
import cmc.mellyserver.auth.client.KakaoClient;
import cmc.mellyserver.auth.client.NaverClient;
import cmc.mellyserver.auth.presentation.dto.AuthRequest;
import cmc.mellyserver.auth.presentation.dto.AuthRequestForOAuthSignup;
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
    private final GoogleClient googleClient;
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
            case GOOGLE:
                    socialUser = googleClient.getUserData(authRequest.getAccessToken());
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
            // 사용자가 가입을 진행하다가 중간에 설정이 귀찮아서 나갔다가 다시 들어올 수도 있음
            // 닉네임은 필수 정보 이기때문에 현재 DB에 저장된 소셜 로그인 유저가 닉네임을 가지고 있는지 여부로 회원가입 여부 판단
            if(user.get().getNickname() != null)
            {
                return new OAuthLoginResponseDto(accessToken,false,user.get());
            }

        }

        userRepository.save(socialUser);

        return new OAuthLoginResponseDto(accessToken,true, socialUser);

    }

    private String getToken(String userId)
    {
        AuthToken accessToken = jwtTokenProvider.createToken(userId, RoleType.USER, "100000000");
        return accessToken.getToken();
    }

    public void signup(AuthRequestForOAuthSignup authRequestForOAuthSignup) {

    }
}
