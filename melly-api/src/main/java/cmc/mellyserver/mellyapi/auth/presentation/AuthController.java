package cmc.mellyserver.mellyapi.auth.presentation;

import cmc.mellyserver.mellyapi.auth.application.AuthService;
import cmc.mellyserver.mellyapi.auth.application.OAuthService;
import cmc.mellyserver.mellyapi.auth.application.dto.request.ChangePasswordRequest;
import cmc.mellyserver.mellyapi.auth.application.dto.response.TokenResponseDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.*;
import cmc.mellyserver.mellyapi.auth.presentation.dto.response.OAuthResponseDto;
import cmc.mellyserver.mellyapi.common.code.SuccessCode;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.common.util.HeaderUtil;
import cmc.mellyserver.mellycore.infrastructure.email.certification.EmailCertificationRequest;
import cmc.mellyserver.mellycore.infrastructure.email.certification.EmailCertificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final OAuthService oAuthService;

    private final AuthService authService;

    private final EmailCertificationService emailCertificationService;

    // OAuth2를 사용한 소셜 인증
    @PostMapping("/social")
    public ResponseEntity<ApiResponse> socialLogin(@Valid @RequestBody OAuthLoginRequest oAuthLoginRequest) {

        OAuthResponseDto oAuthResponseDto = oAuthService.login(oAuthLoginRequest.toDto());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, oAuthResponseDto);
    }

    @PostMapping("/social-signup")
    public ResponseEntity<ApiResponse> socialSignup(@Valid @RequestBody OAuthSignupRequest oAuthSignupRequest) {

        TokenResponseDto tokenResponseDto = oAuthService.signup(oAuthSignupRequest.toDto());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, tokenResponseDto);
    }


    // 이메일 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid CommonSignupRequest commonSignupRequest) {

        TokenResponseDto signupToken = authService.emailSignup(commonSignupRequest.toDto());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, signupToken);
    }

    // 이메일 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthLoginRequest authLoginRequest) {

        TokenResponseDto loginToken = authService.login(authLoginRequest.toDto());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, loginToken);
    }

    // 이메일 유효성을 파악하기 위해 인증번호 전송
    @PostMapping("/email-certification/sends")
    public ResponseEntity<ApiResponse> sendEmailCertification(@RequestBody EmailCertificationRequest requestDto) {
        emailCertificationService.sendEmailForCertification(requestDto.getEmail());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    // 인증번호 재전송
    @PostMapping("/email-certification/resends")
    public ResponseEntity<ApiResponse> resendEmailCertification(@RequestBody EmailCertificationRequest requestDto) {
        emailCertificationService.sendEmailForCertification(requestDto.getEmail());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    // 인증번호 확인
    @PostMapping("/email-certification/confirms")
    public ResponseEntity<ApiResponse> emailVerification(@RequestBody EmailCertificationRequest requestDto) {
        emailCertificationService.verifyEmail(requestDto);
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    // 닉네임 중복 체크
    @GetMapping("/user-nicknames/{nickname}/exists")
    public ResponseEntity<ApiResponse> checkNicknameDuplicate(@PathVariable String nickname) {

        authService.checkDuplicatedNickname(nickname);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS);
    }

    // 이메일 중복 체크
    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<ApiResponse> checkEmailDuplicate(@PathVariable String email) {

        authService.checkDuplicatedEmail(email);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS);
    }

    @PatchMapping("/forget/password")
    public ResponseEntity<ApiResponse> changePasswordByForget(@Valid @RequestBody ChangePasswordRequest requestDto) {
        authService.updateForgetPassword(requestDto);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }


    @PatchMapping("/password")
    public ResponseEntity<ApiResponse> changePassword(@CurrentUser LoginUser loginUser, @Valid @RequestBody ChangePasswordRequest requestDto) {
        authService.changePassword(loginUser.getId(), requestDto);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    // 로그 아웃
    @PatchMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@CurrentUser LoginUser loginUser, HttpServletRequest request) {

        authService.logout(loginUser.getId(), HeaderUtil.getAccessToken(request));
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse> withdraw(@CurrentUser LoginUser loginUser, HttpServletRequest request) {

        authService.withdraw(loginUser.getId(), HeaderUtil.getAccessToken(request));
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @PostMapping("/token/reissue")
    public ResponseEntity<ApiResponse> generateAccessToken(@RequestBody ReIssueAccessTokenRequest reIssueAccessTokenRequest) {

        TokenResponseDto tokenResponseDto = authService.reIssueAccessTokenAndRefreshToken(reIssueAccessTokenRequest.getRefreshToken());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, tokenResponseDto);
    }
}
