package cmc.mellyserver.mellyapi.auth.presentation;

import cmc.mellyserver.mellyapi.auth.application.AuthService;
import cmc.mellyserver.mellyapi.auth.application.OAuthService;
import cmc.mellyserver.mellyapi.auth.application.dto.request.ChangePasswordRequest;
import cmc.mellyserver.mellyapi.auth.application.dto.response.TokenResponseDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.AuthLoginRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.CommonSignupRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.OAuthLoginRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.ReIssueAccessTokenRequest;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.common.util.HeaderUtil;
import cmc.mellyserver.mellyinfra.email.EmailCertificationRequest;
import cmc.mellyserver.mellyinfra.email.EmailCertificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.CREATED;
import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.OK;
import static cmc.mellyserver.mellyapi.common.response.ApiResponse.OK;

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

        String authToken = oAuthService.login(oAuthLoginRequest.toDto());
        return OK(authToken);
    }

    // 이메일 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid CommonSignupRequest commonSignupRequest) {

        TokenResponseDto signupToken = authService.emailSignup(commonSignupRequest.toDto());
        return OK(signupToken);
    }

    // 이메일 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthLoginRequest authLoginRequest) {

        TokenResponseDto loginToken = authService.login(authLoginRequest.toDto());
        return OK(loginToken);
    }

    // 이메일 유효성을 파악하기 위해 인증번호 전송
    @PostMapping("/email-certification/sends")
    public ResponseEntity sendEmailCertification(@RequestBody EmailCertificationRequest requestDto) {
        emailCertificationService.sendEmailForCertification(requestDto.getEmail());
        return CREATED;
    }

    // 인증번호 재전송
    @PostMapping("/email-certification/resends")
    public ResponseEntity resendEmailCertification(@RequestBody EmailCertificationRequest requestDto) {
        emailCertificationService.sendEmailForCertification(requestDto.getEmail());
        return CREATED;
    }

    // 인증번호 확인
    @PostMapping("/email-certification/confirms")
    public void emailVerification(@RequestBody EmailCertificationRequest requestDto) {
        emailCertificationService.verifyEmail(requestDto);
    }

    // 닉네임 중복 체크
    @GetMapping("/user-nicknames/{nickname}/exists")
    public ResponseEntity<ApiResponse> checkNicknameDuplicate(@PathVariable String nickname) {

        authService.checkDuplicatedNickname(nickname);
        return OK;
    }

    // 이메일 중복 체크
    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<ApiResponse> checkEmailDuplicate(@PathVariable String email) {

        authService.checkDuplicatedEmail(email);
        return OK;
    }

    @PatchMapping("/forget/password")
    public void changePasswordByForget(@Valid @RequestBody ChangePasswordRequest requestDto) {
        authService.updatePasswordByForget(requestDto);
    }


    @PatchMapping("/password")
    public void changePassword(@CurrentUser LoginUser loginUser, @Valid @RequestBody ChangePasswordRequest requestDto) {
        authService.updatePassword(loginUser.getId(), requestDto);
    }

    // 로그 아웃
    @PutMapping("/logout")
    public ResponseEntity logout(@CurrentUser LoginUser loginUser, HttpServletRequest request) {

        authService.logout(loginUser.getId(), HeaderUtil.getAccessToken(request));
        return ResponseEntity.noContent().build();
    }

    // 회원 탈퇴
    @PutMapping("/withdraw")
    public ResponseEntity withdraw(@CurrentUser LoginUser loginUser, HttpServletRequest request) {

        authService.withdraw(loginUser.getId(), HeaderUtil.getAccessToken(request));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/access-token/reissue")
    public ResponseEntity<ApiResponse> generateAccessToken(@RequestBody ReIssueAccessTokenRequest reIssueAccessTokenRequest) {

        TokenResponseDto tokenResponseDto = authService.reIssueAccessToken(reIssueAccessTokenRequest.getRefreshToken());
        return OK(tokenResponseDto);
    }
}
