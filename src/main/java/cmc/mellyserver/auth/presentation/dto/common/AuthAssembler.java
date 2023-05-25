package cmc.mellyserver.auth.presentation.dto.common;

import cmc.mellyserver.auth.application.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.auth.application.dto.response.LoginResponseDto;
import cmc.mellyserver.auth.application.dto.response.SignupResponseDto;
import cmc.mellyserver.auth.presentation.dto.response.*;
import cmc.mellyserver.common.enums.Provider;
import cmc.mellyserver.auth.presentation.dto.request.AuthRequestForSignup;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.common.enums.RoleType;
import cmc.mellyserver.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class AuthAssembler {

   public static User createEmailLoginUser(AuthSignupRequestDto authRequestForSignupDto, PasswordEncoder passwordEncoder, String filename)
   {
       String encode = passwordEncoder.encode(authRequestForSignupDto.getPassword());
       return User.builder()
               .email(authRequestForSignupDto.getEmail())
               .password(encode)
               .roleType(RoleType.USER)
               .profileImage(filename)
               .ageGroup(authRequestForSignupDto.getAgeGroup())
               .gender(authRequestForSignupDto.getGender())
               .fcmToken(authRequestForSignupDto.getFcmToken())
               .uid(UUID.randomUUID().toString())
               .provider(Provider.DEFAULT)
               // TODO : 처음 사용자는 앱 푸시 전부 동의 상태로 만들기 -> 차후에 푸시 동의 기능 추가
               .enableAppPush(true)
               .enableComment(true)
               .enableComment(true)
               .nickname(authRequestForSignupDto.getNickname())
               .build();
   }


   public static AuthSignupRequestDto authRequestForSignupDto(AuthRequestForSignup authRequestForSignup)
    {
        return new AuthSignupRequestDto(authRequestForSignup.getEmail(),
                authRequestForSignup.getPassword(),
                authRequestForSignup.getNickname(),
                authRequestForSignup.getAgeGroup(),
                authRequestForSignup.getGender(),
                authRequestForSignup.getFcmToken(),
                authRequestForSignup.getProfileImage());
    }

    public static CommonResponse authUserDataResponse(User user)
    {
        return new CommonResponse(200,"인증 유저 정보",new AuthmeWrappingDto(new AccessTokenUserData(user.getUserSeq(),

                user.getUserId(),
                user.getProvider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                user.getGender(),
                user.getAgeGroup())
                ));
    }

    public static AuthResponseForLogin loginResponse(String accessToken, User user)
    {
        return new AuthResponseForLogin(new AccessTokenUserData(user.getUserSeq(),
                user.getUserId(),
                user.getProvider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                user.getGender(),
                user.getAgeGroup()),
                accessToken);
    }

    public static SignupResponse signupResponse(SignupResponseDto signupResponseDto)
    {
        return SignupResponse.builder()
                .user(signupResponseDto.getUser())
                .token(signupResponseDto.getToken())
                .build();
    }

    public static LoginResponse loginResponse(LoginResponseDto loginResponseDto)
    {
        return LoginResponse.builder()
                .user(loginResponseDto.getUser())
                .token(loginResponseDto.getToken())
                .build();
    }

    public static CommonResponse departNewUser(String token, boolean isNewUser, User user)
    {
        if(isNewUser == true)
        {
            return new CommonResponse(200,
                    "회원가입 필요",
                    new OAuthLoginResponse(AccessTokenUserData.from(user),token,isNewUser)
            );
        }
        else{
            return new CommonResponse(200,
                    "성공",
                    new OAuthLoginResponse(AccessTokenUserData.from(user), token, isNewUser)
            );
        }
    }
}
