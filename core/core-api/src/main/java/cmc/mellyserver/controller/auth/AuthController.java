package cmc.mellyserver.controller.auth;

import cmc.mellyserver.common.code.SuccessCode;
import cmc.mellyserver.common.util.HeaderUtil;
import cmc.mellyserver.controller.auth.dto.common.CurrentUser;
import cmc.mellyserver.controller.auth.dto.common.LoginUser;
import cmc.mellyserver.controller.auth.dto.request.*;
import cmc.mellyserver.controller.auth.dto.response.OAuthResponseDto;
import cmc.mellyserver.domain.auth.AuthService;
import cmc.mellyserver.domain.auth.OAuthService;
import cmc.mellyserver.domain.auth.certification.CertificationService;
import cmc.mellyserver.domain.auth.certification.EmailCertificationRequest;
import cmc.mellyserver.domain.auth.dto.request.ChangePasswordRequest;
import cmc.mellyserver.domain.auth.dto.response.TokenResponseDto;
import cmc.mellyserver.support.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final OAuthService oAuthService;

    private final AuthService authService;

    private final CertificationService certificationService;

    // OAuth2를 사용한 소셜 인증
    @PostMapping("/social")
    public ResponseEntity<ApiResponse<OAuthResponseDto>> socialLogin(@Valid @RequestBody OAuthLoginRequest oAuthLoginRequest) {

        OAuthResponseDto oAuthResponseDto = oAuthService.login(oAuthLoginRequest.toDto());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, oAuthResponseDto);
    }

    @PostMapping("/social-signup")
    public ResponseEntity<ApiResponse<TokenResponseDto>> socialSignup(@Valid @RequestBody OAuthSignupRequest oAuthSignupRequest) {

        TokenResponseDto tokenResponseDto = oAuthService.signup(oAuthSignupRequest.toDto());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, tokenResponseDto);
    }


    // 이메일 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<TokenResponseDto>> signup(@Valid CommonSignupRequest commonSignupRequest) {

        TokenResponseDto signupToken = authService.signup(commonSignupRequest.toDto());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, signupToken);
    }

    // 이메일 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(@Valid @RequestBody AuthLoginRequest authLoginRequest) {

        TokenResponseDto loginToken = authService.login(authLoginRequest.toDto());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, loginToken);
    }

    // 이메일 유효성을 파악하기 위해 인증번호 전송
    @PostMapping("/email-certification/sends")
    public ResponseEntity<ApiResponse<Void>> sendEmailCertification(@RequestBody EmailCertificationRequest requestDto) {

        certificationService.sendCertification(requestDto.getEmail());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    // 인증번호 재전송
    @PostMapping("/email-certification/resends")
    public ResponseEntity<ApiResponse<Void>> resendEmailCertification(@RequestBody EmailCertificationRequest requestDto) {

        certificationService.sendCertification(requestDto.getEmail());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    // 인증번호 확인
    @PostMapping("/email-certification/confirms")
    public ResponseEntity<ApiResponse<Void>> emailVerification(@RequestBody EmailCertificationRequest requestDto) {

        certificationService.verify(requestDto);
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    // 닉네임 중복 체크
    @GetMapping("/user-nicknames/{nickname}/exists")
    public ResponseEntity<ApiResponse<Void>> checkNicknameDuplicate(@PathVariable String nickname) {

        authService.checkDuplicatedNickname(nickname);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS);
    }

    // 이메일 중복 체크
    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<ApiResponse<Void>> checkEmailDuplicate(@PathVariable String email) {

        authService.checkDuplicatedEmail(email);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS);
    }

    @PatchMapping("/forget/password")
    public ResponseEntity<ApiResponse<Void>> changePasswordByForget(@Valid @RequestBody ChangePasswordRequest requestDto) {
        authService.updateForgetPassword(requestDto);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }


    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@CurrentUser LoginUser loginUser, @Valid @RequestBody ChangePasswordRequest requestDto) {
        authService.changePassword(loginUser.getId(), requestDto);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    // 로그 아웃
    @PatchMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@CurrentUser LoginUser loginUser, HttpServletRequest request) {

        authService.logout(loginUser.getId(), HeaderUtil.getAccessToken(request));
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@CurrentUser LoginUser loginUser, HttpServletRequest request) {

        authService.withdraw(loginUser.getId(), HeaderUtil.getAccessToken(request));
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @PostMapping("/token/reissue")
    public ResponseEntity<ApiResponse<TokenResponseDto>> generateAccessToken(@RequestBody ReIssueAccessTokenRequest reIssueAccessTokenRequest) {

        TokenResponseDto tokenResponseDto = authService.reIssueAccessTokenAndRefreshToken(reIssueAccessTokenRequest.getRefreshToken());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, tokenResponseDto);
    }
}
