package cmc.mellyserver.auth.presentation;


import cmc.mellyserver.auth.application.AuthService;
import cmc.mellyserver.auth.application.dto.response.LoginResponseDto;
import cmc.mellyserver.auth.application.dto.response.OAuthLoginResponseDto;
import cmc.mellyserver.auth.application.dto.response.SignupResponseDto;
import cmc.mellyserver.auth.application.impl.AuthServiceImpl;
import cmc.mellyserver.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.auth.presentation.dto.request.*;
import cmc.mellyserver.auth.presentation.dto.response.AuthResponseForLogin;
import cmc.mellyserver.auth.presentation.dto.response.SignupResponse;
import cmc.mellyserver.common.util.HeaderUtil;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import cmc.mellyserver.auth.application.impl.OAuthServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

      private final OAuthServiceImpl oAuthService;

      private final AuthService authService;

      private final AuthenticatedUserChecker authenticatedUserChecker;

      @Operation(summary = "소셜 로그인")
      @PostMapping("/social")
      public ResponseEntity<CommonResponse> socialLogin(@Valid @RequestBody OAuthLoginRequest oAuthLoginRequest)
      {
          OAuthLoginResponseDto oAuthLoginResponse = oAuthService.login(oAuthLoginRequest);
          return ResponseEntity.ok(AuthAssembler.departNewUser(oAuthLoginResponse.getAccessToken(), oAuthLoginResponse.getIsNewUser(), oAuthLoginResponse.getUser()));
      }

      @PostMapping("/login")
      public ResponseEntity<CommonResponse> normalLogin(@Valid @RequestBody AuthLoginRequest authLoginRequest) {
         LoginResponseDto loginResponseDto = authService.login(authLoginRequest.toServiceDto());
         return ResponseEntity.ok(new CommonResponse<>(200,"성공",AuthAssembler.loginResponse(loginResponseDto)));
      }


      @PostMapping("/signup")
      public ResponseEntity<CommonResponse> normalSignup(@Valid @RequestBody CommonSignupRequest commonSignupRequest) {
        SignupResponseDto signupResponseDto = authService.signup(commonSignupRequest.toServiceDto());
        return ResponseEntity.ok(new CommonResponse(200, "성공",AuthAssembler.signupResponse(signupResponseDto)));
      }


      @Operation(summary = "중복 닉네임 체크")
      @PostMapping("/nickname")
      public ResponseEntity<CommonResponse> checkNicknameDuplicate(@RequestBody CheckDuplicateNicknameRequest checkDuplicateNicknameRequest)
      {
          authService.checkDuplicatedNickname(checkDuplicateNicknameRequest.getNickname());
          return ResponseEntity.ok(new CommonResponse(200,"사용해도 좋은 닉네임입니다."));
      }


      @Operation(summary = "중복 이메일 체크")
      @PostMapping ("/email")
      public ResponseEntity<CommonResponse> checkEmailDuplicate(@RequestBody CheckDuplicateEmailRequest checkDuplicateEmailRequest)
      {
          authService.checkDuplicatedEmail(checkDuplicateEmailRequest.getEmail());
          return ResponseEntity.ok(new CommonResponse(200,"사용해도 좋은 이메일입니다."));
      }


      @Operation(summary = "로그아웃", description = "- 로그아웃 시 기존의 액세스 토큰은 서버 단에서 재활용 불가 처리를 합니다." +
                                                  "- 로그아웃 로직은 로그인한 유저가 사용할 수 있는 기능이므로 Header에 토큰 넣어주세요!" +
                                                  "- 일반 로그인, 소셜 로그인 공통 사용 가능")
      @DeleteMapping("/logout")
      public ResponseEntity<CommonResponse> logout(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                                                   HttpServletRequest request)
      {
          authService.logout(Long.parseLong(user.getUsername()), HeaderUtil.getAccessToken(request));
          return ResponseEntity.ok(new CommonResponse(200,"로그아웃 완료"));
      }


      @Operation(summary = "회원 탈퇴")
      @DeleteMapping("/withdraw")
      public ResponseEntity<CommonResponse> withdraw(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                                                     HttpServletRequest request)
      {
          authService.withdraw(Long.parseLong(user.getUsername()),HeaderUtil.getAccessToken(request));
          return ResponseEntity.ok(new CommonResponse(200,"회원탈퇴 완료"));
      }





}
