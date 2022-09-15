package cmc.mellyserver.auth.presentation;

import cmc.mellyserver.auth.application.AuthService;
import cmc.mellyserver.auth.application.OAuthLoginResponseDto;
import cmc.mellyserver.auth.application.OAuthService;
import cmc.mellyserver.auth.presentation.dto.*;
import cmc.mellyserver.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

      private final OAuthService oAuthService;
      private final AuthService authService;

      @Operation(summary = "소셜 로그인")
      @PostMapping("/social")
      public ResponseEntity<OAuthLoginResponse> socialLogin(@RequestBody AuthRequest authRequest)
      {
          OAuthLoginResponseDto response = oAuthService.login(authRequest);
          return ResponseEntity.ok(new OAuthLoginResponse(response.getAccessToken(),response.getIsSignup()));
      }

      // TODO : 성공했을때 API 정하기!
      @Operation(summary = "일반 이메일 회원가입")
      @PostMapping("/signup")
      public ResponseEntity<SignupResponse> emailLoginSignup(@RequestBody AuthRequestForSignup authRequestForSignup)
      {
          User user = authService.signup(authRequestForSignup.getEmail(), authRequestForSignup.getPassword());
          return ResponseEntity.ok(new SignupResponse(user.getEmail()));
      }

      @Operation(summary = "일반 이메일 로그인")
      @PostMapping("/login")
      public ResponseEntity<AccessTokenResponse> emailLogin(@RequestBody AuthRequestForLogin authRequestForLogin)
      {
          String accessToken = authService.login(authRequestForLogin.getEmail(), authRequestForLogin.getPassword());
          return ResponseEntity.ok(new AccessTokenResponse(accessToken));
      }

      @Operation(summary = "일반 이메일 로그아웃")
      @DeleteMapping("/logout")
      public void emailLogout(@RequestBody LogoutRequest logoutRequest)
      {
         authService.logout(logoutRequest.getAccessToken());
      }
}
