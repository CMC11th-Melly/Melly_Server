package cmc.mellyserver.auth.presentation.dto;

import cmc.mellyserver.auth.application.dto.AuthRequestForSignupDto;
import cmc.mellyserver.common.CommonResponse;
import cmc.mellyserver.user.domain.User;

public class AuthAssembler {

   public static AuthRequestForSignupDto authRequestForSignupDto(AuthRequestForSignup authRequestForSignup)
    {
        return new AuthRequestForSignupDto(authRequestForSignup.getEmail(),
                authRequestForSignup.getPassword(),
                authRequestForSignup.getNickname(),
                authRequestForSignup.getAgeGroup(),
                authRequestForSignup.getGender(),
                authRequestForSignup.getProfile_image());
    }

    public static CommonResponse authUserDataResponse(User user)
    {
        return new CommonResponse(200,"인증 유저 정보",new AccessTokenUserData(user.getUserSeq(),
                user.getUserId(),
                user.getProvider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                user.getGender(),
                user.getAgeGroup())
                );
    }

    public static  LoginResponse loginResponse(String accessToken,User user)
    {
        return new LoginResponse(new AccessTokenUserData(user.getUserSeq(),
                user.getUserId(),
                user.getProvider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                user.getGender(),
                user.getAgeGroup()),
                accessToken);
    }

    public static CommonResponse checkDuplicateEmailResponse(boolean isDuplicated)
    {
        if(isDuplicated == true)
        {
            return new CommonResponse(400,"이미 존재하는 이메일입니다");
        }
        else{
            return new CommonResponse(200,"사용해도 좋은 이메일입니다");
        }
    }

    public static CommonResponse checkDuplicateNicknameResponse(boolean isDuplicated)
    {
        if(isDuplicated == true)
        {
            return new CommonResponse(400,"이미 존재하는 닉네임입니다");
        }
        else{
            return new CommonResponse(200,"사용해도 좋은 닉네임입니다");
        }
    }

    public static CommonResponse oAuthLoginResponse(String token, boolean isNewUser, User user)
    {
        if(isNewUser == true)
        {
            return new CommonResponse(200,
                    "회원가입이 필요합니다",
                    new OAuthLoginResponse(
                            isNewUser,AccessTokenUserData.builder().uid(user.getUserId()).build())
            );
        }
        else{
            return new CommonResponse(200,
                    "로그인 완료",
                    new OAuthLoginResponse(new AccessTokenUserData(user.getUserSeq(),
                            user.getUserId(),
                            user.getProvider(),
                            user.getEmail(),
                            user.getNickname(),
                            user.getProfileImage(),
                            user.getGender(),
                            user.getAgeGroup()),
                            token,
                            isNewUser)
                    );
        }
    }
}
