package cmc.mellyserver.auth.presentation;


import cmc.mellyserver.auth.application.AuthService;
import cmc.mellyserver.auth.application.dto.OAuthLoginResponseDto;
import cmc.mellyserver.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.auth.presentation.dto.request.*;
import cmc.mellyserver.auth.presentation.dto.response.AuthResponseForLogin;
import cmc.mellyserver.auth.presentation.dto.response.SignupResponse;
import cmc.mellyserver.auth.util.HeaderUtil;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import cmc.mellyserver.auth.application.OAuthService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

      private final OAuthService oAuthService;
      private final AuthService authService;
      private final AuthenticatedUserChecker authenticatedUserChecker;


      @Operation(summary = "소셜 로그인 시 회원가입")
      @PostMapping("/social/signup")
      public ResponseEntity<CommonResponse<AuthResponseForLogin>> socialSignup(AuthRequestForOAuthSignup authRequestForOAuthSignup)
      {
          AuthResponseForLogin login = oAuthService.signup(authRequestForOAuthSignup);
          return ResponseEntity.ok(new CommonResponse<>(200,"로그인 완료",login));
      }


      @Operation(summary = "소셜 로그인",description = "소셜 로그인 타입에 따라 로그인 방식을 분기")
      @PostMapping("/social")
      public ResponseEntity<CommonResponse<?>> socialLogin(@RequestBody AuthRequest authRequest)
      {
          OAuthLoginResponseDto response = oAuthService.login(authRequest);

          return ResponseEntity.ok(AuthAssembler.oAuthLoginResponse(response.getAccessToken(),response.getIsNewUser(),response.getUser()));
      }


      @Operation(summary = "일반 이메일 회원가입",description = "- 성별의 경우 true는 남성, false는 여성입니다." +
                                                              "\n- 데이터 보내주실때 프로필 사진 고려해서 **multipart/formdata** 형식으로 보내주세요! <br />" +
                                                              "\n- 연령대는 enum으로 처리하기 위해서 10대부터 70대 이상까지 차례대로 **ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN** 으로 설정했습니다."
      )
      @PostMapping("/signup")
      public ResponseEntity<CommonResponse> emailLoginSignup(@Valid AuthRequestForSignup authRequestForSignup)
      {
          SignupResponse signup = authService.signup(AuthAssembler.authRequestForSignupDto(authRequestForSignup));
          return ResponseEntity.ok(new CommonResponse(200, "회원가입 완료",signup));
      }


      @Operation(summary = "일반 이메일 로그인")
      @PostMapping("/login")
      public ResponseEntity<CommonResponse<AuthResponseForLogin>> emailLogin(@RequestBody AuthRequestForLogin authRequestForLogin)
      {
          AuthResponseForLogin login = authService.login(authRequestForLogin.getEmail(), authRequestForLogin.getPassword(),authRequestForLogin.getFcmToken());
          return ResponseEntity.ok(new CommonResponse<>(200,"로그인 완료",login));
      }


      @Operation(summary = "중복 닉네임 체크")
      @PostMapping("/nickname")
      public ResponseEntity<CommonResponse> checkNicknameDuplicate(@RequestBody CheckDuplicateNicknameRequest checkDuplicateNicknameRequest)
      {
          authService.checkNicknameDuplicate(checkDuplicateNicknameRequest.getNickname());
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
          authService.logout(user.getUsername(), HeaderUtil.getAccessToken(request));
          return ResponseEntity.ok(new CommonResponse(200,"로그아웃 완료"));
      }


      @Operation(summary = "회원 탈퇴")
      @DeleteMapping("/withdraw")
      public ResponseEntity<CommonResponse> withdraw(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                                                     HttpServletRequest request)
      {
          authService.withdraw(user.getUsername(),HeaderUtil.getAccessToken(request));
          return ResponseEntity.ok(new CommonResponse(200,"회원탈퇴 완료"));
      }


      @Operation(summary = "유저 정보 조회")
      @GetMapping("/me")
      public ResponseEntity<CommonResponse> getUserData(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user)
      {
          User userData = authenticatedUserChecker.checkAuthenticatedUserExist(user.getUsername());
          return ResponseEntity.ok(AuthAssembler.authUserDataResponse(userData));
      }


}
