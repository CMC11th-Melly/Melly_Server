package cmc.mellyserver.mellyapi.auth.presentation;

import cmc.mellyserver.mellyapi.auth.application.dto.response.LoginResponseDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.OAuthLoginResponseDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.SignupResponseDto;
import cmc.mellyserver.mellyapi.auth.application.impl.AuthService;
import cmc.mellyserver.mellyapi.auth.application.impl.OAuthService;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.AuthLoginRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.CommonSignupRequest;
import cmc.mellyserver.mellyapi.auth.presentation.dto.request.OAuthLoginRequest;
import cmc.mellyserver.mellyapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.common.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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

    @PostMapping("/social")
    public ResponseEntity<ApiResponse> socialLogin(@Valid @RequestBody OAuthLoginRequest oAuthLoginRequest) {

        OAuthLoginResponseDto oAuthLoginResponse = oAuthService.login(AuthAssembler.oAuthLoginRequestDto(oAuthLoginRequest));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                AuthAssembler.departNewUser(oAuthLoginResponse.getAccessToken(), oAuthLoginResponse.getIsNewUser(), oAuthLoginResponse.getUser())));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> normalLogin(@Valid @RequestBody AuthLoginRequest authLoginRequest) {

        LoginResponseDto loginResponseDto = authService.login(AuthAssembler.authLoginRequestDto(authLoginRequest));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, AuthAssembler.loginResponse(loginResponseDto)));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> normalSignup(@Valid CommonSignupRequest commonSignupRequest) throws InterruptedException {

        SignupResponseDto signupResponseDto = authService.signup(AuthAssembler.authSignupRequestDto(commonSignupRequest));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, AuthAssembler.signupResponse(signupResponseDto)));
    }

    // 상태를 변경하는게 아니라 단순 체크이므로 get을 사용하는게 맞다
    @GetMapping("/user-nicknames/{nickname}/exists")
    public ResponseEntity<ApiResponse> checkNicknameDuplicate(@PathVariable String nickname) {

        authService.checkDuplicatedNickname(nickname);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<ApiResponse> checkEmailDuplicate(@PathVariable String email) {

        authService.checkDuplicatedEmail(email);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PutMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@AuthenticationPrincipal User user, HttpServletRequest request) {

        authService.logout(Long.parseLong(user.getUsername()), HeaderUtil.getAccessToken(request));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PutMapping("/withdraw")
    public ResponseEntity withdraw(@AuthenticationPrincipal User user, HttpServletRequest request) {

        authService.withdraw(Long.parseLong(user.getUsername()), HeaderUtil.getAccessToken(request));
        return ResponseEntity.noContent().build();
    }

}
