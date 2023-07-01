package cmc.mellyserver.mellyappexternalapi.auth.presentation;

import cmc.mellyserver.mellyappexternalapi.auth.application.AuthService;
import cmc.mellyserver.mellyappexternalapi.auth.application.OAuthService;
import cmc.mellyserver.mellyappexternalapi.auth.application.dto.response.LoginResponseDto;
import cmc.mellyserver.mellyappexternalapi.auth.application.dto.response.OAuthLoginResponseDto;
import cmc.mellyserver.mellyappexternalapi.auth.application.dto.response.SignupResponseDto;
import cmc.mellyserver.mellyappexternalapi.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.mellyappexternalapi.auth.presentation.dto.request.*;
import cmc.mellyserver.mellyappexternalapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyappexternalapi.common.response.CommonResponse;
import cmc.mellyserver.mellyappexternalapi.common.util.HeaderUtil;
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
    public ResponseEntity<CommonResponse> socialLogin(@Valid @RequestBody OAuthLoginRequest oAuthLoginRequest) {

        OAuthLoginResponseDto oAuthLoginResponse = oAuthService.login(AuthAssembler.oAuthLoginRequestDto(oAuthLoginRequest));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                AuthAssembler.departNewUser(oAuthLoginResponse.getAccessToken(), oAuthLoginResponse.getIsNewUser(), oAuthLoginResponse.getUser())));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> normalLogin(@Valid @RequestBody AuthLoginRequest authLoginRequest) {

        LoginResponseDto loginResponseDto = authService.login(AuthAssembler.authLoginRequestDto(authLoginRequest));
        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, AuthAssembler.loginResponse(loginResponseDto)));
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> normalSignup(@Valid CommonSignupRequest commonSignupRequest) throws InterruptedException {

        SignupResponseDto signupResponseDto = authService.signup(AuthAssembler.authSignupRequestDto(commonSignupRequest));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, AuthAssembler.signupResponse(signupResponseDto)));
    }

    @PostMapping("/nickname")
    public ResponseEntity<CommonResponse> checkNicknameDuplicate(@RequestBody CheckDuplicateNicknameRequest checkDuplicateNicknameRequest) {

        authService.checkDuplicatedNickname(checkDuplicateNicknameRequest.getNickname());
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PostMapping("/email")
    public ResponseEntity<CommonResponse> checkEmailDuplicate(@RequestBody CheckDuplicateEmailRequest checkDuplicateEmailRequest) {

        authService.checkDuplicatedEmail(checkDuplicateEmailRequest.getEmail());
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PutMapping("/logout")
    public ResponseEntity<CommonResponse> logout(@AuthenticationPrincipal User user, HttpServletRequest request) {

        authService.logout(Long.parseLong(user.getUsername()), HeaderUtil.getAccessToken(request));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PutMapping("/withdraw")
    public ResponseEntity withdraw(@AuthenticationPrincipal User user, HttpServletRequest request) {

        authService.withdraw(Long.parseLong(user.getUsername()), HeaderUtil.getAccessToken(request));
        return ResponseEntity.noContent().build();
    }

}
