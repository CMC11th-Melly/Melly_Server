package cmc.mellyserver.auth.presentation.dto;

import cmc.mellyserver.auth.application.dto.AuthRequestForSignupDto;
import cmc.mellyserver.user.domain.User;

public class AuthAssembler {

   public static AuthRequestForSignupDto authRequestForSignupDto(AuthRequestForSignup authRequestForSignup)
    {
        return new AuthRequestForSignupDto(authRequestForSignup.getEmail(),
                authRequestForSignup.getPassword(),
                authRequestForSignup.getNickname(),
                authRequestForSignup.getAgeGroup(),
                authRequestForSignup.isGender(),
                authRequestForSignup.getProfile_image());
    }

    public static AuthUserDataResponse authUserDataResponse(User user)
    {
        return new AuthUserDataResponse(user.getUserSeq(),
                user.getUserId(),
                user.getProvider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                user.isGender(),
                user.getAgeGroup()
                );
    }

    public static AccessTokenResponse accessTokenResponse(String accessToken,User user)
    {
        return new AccessTokenResponse(200,"로그인 완료.",new AccessTokenUserData(user.getUserSeq(),
                user.getUserId(),
                user.getProvider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                user.isGender(),
                user.getAgeGroup()),
                accessToken);
    }

    public static CheckDuplicateEmailResponse checkDuplicateEmailResponse(boolean isDuplicated)
    {
        if(isDuplicated == true)
        {
            return new CheckDuplicateEmailResponse(400,"이미 존재하는 이메일입니다");
        }
        else{
            return new CheckDuplicateEmailResponse(200,"사용해도 좋은 이메일입니다");
        }
    }

    public static CheckDuplicateNicknameResponse checkDuplicateNicknameResponse(boolean isDuplicated)
    {
        if(isDuplicated == true)
        {
            return new CheckDuplicateNicknameResponse(400,"이미 존재하는 닉네임입니다");
        }
        else{
            return new CheckDuplicateNicknameResponse(200,"사용해도 좋은 닉네임입니다");
        }
    }

    public static OAuthLoginResponse oAuthLoginResponse(String token, boolean isNewUser, User user)
    {
        if(isNewUser == true)
        {
            return new OAuthLoginResponse(200,
                    "회원가입이 필요합니다",
                    new AccessTokenUserData(user.getUserSeq(),
                            user.getUserId(),
                            user.getProvider(),
                            user.getEmail(),
                            user.getNickname(),
                            user.getProfileImage(),
                            user.isGender(),
                            user.getAgeGroup()),
                    token,
                    isNewUser);
        }
        else{
            return new OAuthLoginResponse(200,
                    "로그인 완료",
                    new AccessTokenUserData(user.getUserSeq(),
                            user.getUserId(),
                            user.getProvider(),
                            user.getEmail(),
                            user.getNickname(),
                            user.getProfileImage(),
                            user.isGender(),
                            user.getAgeGroup()),
                    token,
                    isNewUser);
        }
    }
}
