package cmc.mellyserver.mellyapi.auth.presentation.dto.common;

import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.LoginResponseDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.SignupResponseDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.AuthLoginRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.AuthRequestForSignup;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.CommonSignupRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.OAuthLoginRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.response.*;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellycommon.enums.Provider;
import cmc.mellyserver.mellycommon.enums.RoleType;
import cmc.mellyserver.mellycore.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class AuthAssembler {

    public static User createEmailLoginUser(AuthSignupRequestDto authRequestForSignupDto,
                                            PasswordEncoder passwordEncoder, String filename) {
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
                .enableAppPush(true)
                .enableComment(true)
                .enableComment(true)
                .nickname(authRequestForSignupDto.getNickname())
                .build();
    }

    public static AuthSignupRequestDto authRequestForSignupDto(
            AuthRequestForSignup authRequestForSignup) {
        return new AuthSignupRequestDto(authRequestForSignup.getEmail(),
                authRequestForSignup.getPassword(),
                authRequestForSignup.getNickname(),
                authRequestForSignup.getAgeGroup(),
                authRequestForSignup.getGender(),
                authRequestForSignup.getFcmToken(),
                authRequestForSignup.getProfileImage());
    }

    public static ApiResponse authUserDataResponse(User user) {
        return new ApiResponse(200, "인증 유저 정보",
                new AuthmeWrappingDto(new AccessTokenUserData(user.getUserSeq(),

                        user.getUserId(),
                        user.getProvider(),
                        user.getEmail(),
                        user.getNickname(),
                        user.getProfileImage(),
                        user.getGender(),
                        user.getAgeGroup())
                ));
    }

    public static AuthResponseForLogin loginResponse(String accessToken, User user) {
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

    public static SignupResponse signupResponse(SignupResponseDto signupResponseDto) {
        return SignupResponse.builder()
                .user(signupResponseDto.getUser())
                .token(signupResponseDto.getToken())
                .build();
    }

    public static LoginResponse loginResponse(LoginResponseDto loginResponseDto) {
        return LoginResponse.builder()
                .user(loginResponseDto.getUser())
                .token(loginResponseDto.getToken())
                .build();
    }

    public static OAuthLoginRequestDto oAuthLoginRequestDto(OAuthLoginRequest oAuthLoginRequest) {
        return OAuthLoginRequestDto.builder()
                .fcmToken(oAuthLoginRequest.getFcmToken())
                .accessToken(oAuthLoginRequest.getAccessToken())
                .provider(oAuthLoginRequest.getProvider())
                .build();
    }

    public static AuthLoginRequestDto authLoginRequestDto(AuthLoginRequest authLoginRequest) {
        return AuthLoginRequestDto.builder()
                .email(authLoginRequest.getEmail())
                .fcmToken(authLoginRequest.getFcmToken())
                .password(authLoginRequest.getPassword())
                .build();
    }

    public static AuthSignupRequestDto authSignupRequestDto(
            CommonSignupRequest commonSignupRequest) {
        return AuthSignupRequestDto.builder()
                .ageGroup(commonSignupRequest.getAgeGroup())
                .email(commonSignupRequest.getEmail())
                .password(commonSignupRequest.getPassword())
                .fcmToken(commonSignupRequest.getFcmToken())
                .gender(commonSignupRequest.getGender())
                .nickname(commonSignupRequest.getNickname())
                .profile_image(commonSignupRequest.getProfileImage())
                .build();
    }

    public static ApiResponse departNewUser(String token, boolean isNewUser, User user) {
        if (isNewUser == true) {
            return new ApiResponse(200,
                    "회원가입 필요",
                    new OAuthLoginResponse(AccessTokenUserData.from(user), token, isNewUser)
            );
        } else {
            return new ApiResponse(200,
                    "성공",
                    new OAuthLoginResponse(AccessTokenUserData.from(user), token, isNewUser)
            );
        }
    }
}
