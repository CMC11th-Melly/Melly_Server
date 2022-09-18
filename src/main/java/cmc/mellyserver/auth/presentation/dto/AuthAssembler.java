package cmc.mellyserver.auth.presentation.dto;

import cmc.mellyserver.auth.application.dto.AuthRequestForSignupDto;

public class AuthAssembler {

   public static AuthRequestForSignupDto authRequestForSignupDto(AuthRequestForSignup authRequestForSignup)
    {
        return new AuthRequestForSignupDto(authRequestForSignup.getEmail(),
                authRequestForSignup.getPassword(),
                authRequestForSignup.getNickname(),
                authRequestForSignup.isGender(),
                authRequestForSignup.getProfile_image(),
                authRequestForSignup.getBirthday());
    }
}
