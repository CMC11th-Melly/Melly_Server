package cmc.mellyserver.mellyapi.auth.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.mellyapi.auth.application.AuthService;
import cmc.mellyserver.mellyapi.auth.application.OAuthService;
import cmc.mellyserver.mellyapi.auth.application.dto.response.LoginResponseDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.OAuthLoginResponseDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.SignupResponseDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.AuthLoginRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.CheckDuplicateEmailRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.CheckDuplicateNicknameRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.CommonSignupRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.OAuthLoginRequest;
import cmc.mellyserver.mellyapi.common.response.CommonResponse;
import cmc.mellyserver.mellyapi.common.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final OAuthService oAuthService;

	private final AuthService authService;

	@PostMapping("/social")
	public ResponseEntity<CommonResponse> socialLogin(@Valid @RequestBody OAuthLoginRequest oAuthLoginRequest) {
		OAuthLoginResponseDto oAuthLoginResponse = oAuthService.login(
			AuthAssembler.oAuthLoginRequestDto(oAuthLoginRequest));
		return ResponseEntity.ok(
			AuthAssembler.departNewUser(oAuthLoginResponse.getAccessToken(), oAuthLoginResponse.getIsNewUser(),
				oAuthLoginResponse.getUser()));
	}

	@PostMapping("/login")
	public ResponseEntity<CommonResponse> normalLogin(@Valid @RequestBody AuthLoginRequest authLoginRequest) {
		LoginResponseDto loginResponseDto = authService.login(AuthAssembler.authLoginRequestDto(authLoginRequest));
		return ResponseEntity.ok(new CommonResponse<>(200, "성공", AuthAssembler.loginResponse(loginResponseDto)));
	}

	@PostMapping("/signup")
	public ResponseEntity<CommonResponse> normalSignup(@Valid CommonSignupRequest commonSignupRequest) {
		SignupResponseDto signupResponseDto = authService.signup(
			AuthAssembler.authSignupRequestDto(commonSignupRequest));
		return ResponseEntity.ok(new CommonResponse(200, "성공", AuthAssembler.signupResponse(signupResponseDto)));
	}

	@PostMapping("/nickname")
	public ResponseEntity<CommonResponse> checkNicknameDuplicate(
		@RequestBody CheckDuplicateNicknameRequest checkDuplicateNicknameRequest) {
		authService.checkDuplicatedNickname(checkDuplicateNicknameRequest.getNickname());
		return ResponseEntity.ok(new CommonResponse(200, "사용해도 좋은 닉네임입니다."));
	}

	@PostMapping("/email")
	public ResponseEntity<CommonResponse> checkEmailDuplicate(
		@RequestBody CheckDuplicateEmailRequest checkDuplicateEmailRequest) {
		authService.checkDuplicatedEmail(checkDuplicateEmailRequest.getEmail());
		return ResponseEntity.ok(new CommonResponse(200, "사용해도 좋은 이메일입니다."));
	}

	@DeleteMapping("/logout")
	public ResponseEntity<CommonResponse> logout(
		@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
		HttpServletRequest request) {
		authService.logout(Long.parseLong(user.getUsername()), HeaderUtil.getAccessToken(request));
		return ResponseEntity.ok(new CommonResponse(200, "로그아웃 완료"));
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<CommonResponse> withdraw(
		@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
		HttpServletRequest request) {
		authService.withdraw(Long.parseLong(user.getUsername()), HeaderUtil.getAccessToken(request));
		return ResponseEntity.ok(new CommonResponse(200, "회원탈퇴 완료"));
	}

}
