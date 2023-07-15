package cmc.mellyserver.mellyapi.auth.application.impl;

import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.LoginResponseDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.SignupResponseDto;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.mellyapi.common.token.JwtTokenProvider;
import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.common.aws.FileUploader;
import cmc.mellyserver.mellycore.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import cmc.mellyserver.mellycore.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final FileUploader fileUploader;

    private final RedisTemplate redisTemplate;

    private final AuthenticatedUserChecker authenticatedUserChecker;

    @Value("${app.auth.tokenExpiry}")
    private String expiry;

    @Transactional
    public SignupResponseDto signup(AuthSignupRequestDto authSignupRequestDto) {

        checkDuplicatedEmail(authSignupRequestDto.getEmail());
        String filename = fileUploader.getMultipartFileName(authSignupRequestDto.getProfile_image());

        User savedUser = userRepository.save(AuthAssembler.createEmailLoginUser(authSignupRequestDto, passwordEncoder, filename));

        return SignupResponseDto.of(savedUser, jwtTokenProvider.createToken(savedUser.getUserSeq(), savedUser.getRoleType()));
    }


    public LoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {

        User user = checkEmailExist(authLoginRequestDto);
        checkPassword(authLoginRequestDto.getPassword(), user.getPassword());

        user.setFcmToken(authLoginRequestDto.getFcmToken());
        user.updateLastLoginTime(LocalDateTime.now());

        return LoginResponseDto.of(user, jwtTokenProvider.createToken(user.getUserSeq(), user.getRoleType()));
    }


    public void logout(Long userSeq, String accessToken) {

        authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        redisTemplate.opsForValue().set(accessToken, "blackList");
    }


    public void withdraw(Long userSeq, String accessToken) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
        user.remove();
    }


    public void checkDuplicatedNickname(String nickname) {

        if (userRepository.findUserByNickname(nickname).isPresent()) {
            throw new GlobalBadRequestException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }


    public void checkDuplicatedEmail(String email) {

        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new GlobalBadRequestException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private User checkEmailExist(AuthLoginRequestDto authLoginRequestDto) {

        return userRepository.findUserByEmail(authLoginRequestDto.getEmail())
                .orElseThrow(() -> {
                    throw new GlobalBadRequestException(ErrorCode.INVALID_EMAIL);
                });
    }

    private void checkPassword(String password, String originPassword) {

        if (!passwordEncoder.matches(password, originPassword)) {
            throw new GlobalBadRequestException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
