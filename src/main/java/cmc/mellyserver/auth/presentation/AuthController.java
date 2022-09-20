package cmc.mellyserver.auth.presentation;


import cmc.mellyserver.auth.application.AuthService;
import cmc.mellyserver.auth.application.OAuthLoginResponseDto;
import cmc.mellyserver.auth.presentation.dto.*;
import cmc.mellyserver.auth.util.HeaderUtil;
import cmc.mellyserver.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import cmc.mellyserver.auth.application.OAuthService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

      private final OAuthService oAuthService;
      private final AuthService authService;

      @Operation(summary = "소셜 로그인",description = "소셜 로그인 타입에 따라 로그인 방식을 분기")
      @PostMapping("/social")
      public ResponseEntity<OAuthLoginResponse> socialLogin(@RequestBody AuthRequest authRequest)
      {
          OAuthLoginResponseDto response = oAuthService.login(authRequest);
          return ResponseEntity.ok(AuthAssembler.oAuthLoginResponse(response.getAccessToken(),response.getIsNewUser(),response.getUser()));
      }

      // TODO : 성공했을때 API 정하기!
      @Operation(summary = "일반 이메일 회원가입",description = "성별의 경우 true는 남성, false는 여성입니다. 데이터 보내주실때 프로필 사진 고려해서 multipart/formdata 형식으로 보내주세요!")
      @PostMapping("/signup")
      public ResponseEntity<SignupResponse> emailLoginSignup(AuthRequestForSignup authRequestForSignup)
      {
          User user = authService.signup(AuthAssembler.authRequestForSignupDto(authRequestForSignup));
          return ResponseEntity.ok(new SignupResponse(user.getEmail()));
      }

      @Operation(summary = "일반 이메일 로그인")
      @PostMapping("/login")
      public ResponseEntity<AccessTokenResponse> emailLogin(@RequestBody AuthRequestForLogin authRequestForLogin)
      {
          AccessTokenResponse result = authService.login(authRequestForLogin.getEmail(), authRequestForLogin.getPassword());
          return ResponseEntity.ok(result);
      }

      @Operation(summary = "중복 닉네임 체크")
      @PostMapping("/nickname")
      public ResponseEntity<CheckDuplicateNicknameResponse> checkNicknameDuplicate(@RequestBody CheckDuplicateNicknameRequest checkDuplicateNicknameRequest)
      {
          Boolean duplicated = authService.checkNicknameDuplicate(checkDuplicateNicknameRequest.getNickname());
          return ResponseEntity.ok(AuthAssembler.checkDuplicateNicknameResponse(duplicated));
      }

      @Operation(summary = "중복 이메일 체크")
      @PostMapping ("/email")
      public ResponseEntity<CheckDuplicateEmailResponse> checkEmailDuplicate(@RequestBody CheckDuplicateEmailRequest checkDuplicateEmailRequest)
      {
          Boolean duplicated = authService.checkEmailDuplicate(checkDuplicateEmailRequest.getEmail());
        return ResponseEntity.ok(AuthAssembler.checkDuplicateEmailResponse(duplicated));
    }

    @Operation(summary = "일반 이메일 로그아웃")
    @DeleteMapping("/logout")
    public void emailLogout(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user, HttpServletRequest request)
    {
        authService.logout(user.getUsername(), HeaderUtil.getAccessToken(request));
    }

    @Operation(summary = "유저 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<AuthUserDataResponse> getUserData(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user)
    {
        User userData = authService.getUserData(user.getUsername());
        return ResponseEntity.ok(AuthAssembler.authUserDataResponse(userData));
    }
}
