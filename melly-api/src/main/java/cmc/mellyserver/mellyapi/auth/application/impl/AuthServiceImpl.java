package cmc.mellyserver.mellyapi.auth.application.impl;

import cmc.mellyserver.mellyapi.auth.application.AuthService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileUploader fileUploader;

    //    private final RedisTemplate redisTemplate;
    private final AuthenticatedUserChecker authenticatedUserChecker;

    @Value("${app.auth.tokenExpiry}")
    private String expiry;

    @Transactional
    @Override
    public SignupResponseDto signup(AuthSignupRequestDto authSignupRequestDto) throws InterruptedException {

        checkDuplicatedEmail(authSignupRequestDto.getEmail());
        String filename = fileUploader.getMultipartFileName(authSignupRequestDto.getProfile_image());

        User savedUser = userRepository.save(AuthAssembler.createEmailLoginUser(authSignupRequestDto, passwordEncoder, filename));

        return SignupResponseDto.of(savedUser, jwtTokenProvider.createToken(savedUser.getUserSeq(), savedUser.getRoleType()));
    }

    @Override
    public LoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {

        User user = checkEmailExist(authLoginRequestDto);
        checkPassword(authLoginRequestDto.getPassword(), user);
        user.setFcmToken(authLoginRequestDto.getFcmToken());
        return LoginResponseDto.of(user, jwtTokenProvider.createToken(user.getUserSeq(), user.getRoleType()));
    }


    @Override
    public void logout(Long userSeq, String accessToken) {

        authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
//        redisTemplate.opsForValue().set(accessToken, "blackList");
    }

    @Override
    public void withdraw(Long userSeq, String accessToken) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_USER);
        });
        user.remove();
    }

    @Override
    public void checkDuplicatedNickname(String nickname) {

        if (userRepository.findUserByNickname(nickname).isPresent()) {
            throw new GlobalBadRequestException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    @Override
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

    private void checkPassword(String password, User user) {

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new GlobalBadRequestException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
