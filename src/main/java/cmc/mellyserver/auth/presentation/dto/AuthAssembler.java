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
}
