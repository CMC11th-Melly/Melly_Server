package cmc.mellyserver.mellyapi.auth.application;

import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.request.ChangePasswordRequest;
import cmc.mellyserver.mellyapi.common.token.JwtTokenProvider;
import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.comment.application.event.SignupEvent;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import cmc.mellyserver.mellyinfra.message.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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

    private final FCMService fcmService;

    private final RedisTemplate redisTemplate;

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final ApplicationEventPublisher publisher;

    @Value("${app.auth.tokenExpiry}")
    private String expiry;

    @Transactional
    public String signup(AuthSignupRequestDto authSignupRequestDto) {

        String encodedPassword = passwordEncoder.encode(authSignupRequestDto.getPassword()); // 비밀번호 암호화

        User savedUser = userRepository.save(User.createEmailLoginUser(authSignupRequestDto.getEmail(), encodedPassword, authSignupRequestDto.getNickname(), authSignupRequestDto.getAgeGroup(), authSignupRequestDto.getGender()));

        savedUser.updateLastLoginTime(LocalDateTime.now()); // 회원가입 후, 바로 로그인이기 때문에 마지막 로그인 시간 업데이트

        fcmService.saveToken(savedUser.getEmail(), authSignupRequestDto.getFcmToken()); // FCM 토큰 업데이트

        publisher.publishEvent(new SignupEvent(savedUser.getId()));

        return jwtTokenProvider.createToken(savedUser.getId(), savedUser.getRoleType()); // 토큰 값 새로 전송
    }

    @Transactional
    public String login(AuthLoginRequestDto authLoginRequestDto) {

        User user = checkEmail(authLoginRequestDto.getEmail()); // 이메일 검증
        checkPassword(authLoginRequestDto.getPassword(), user.getPassword()); // 비밀번호 일치 여부 검증
        user.updateLastLoginTime(LocalDateTime.now()); // 마지막 로그인 시간 업데이트
        fcmService.saveToken(user.getEmail(), authLoginRequestDto.getFcmToken());
        return jwtTokenProvider.createToken(user.getId(), user.getRoleType());
    }


    public void logout(Long userId, String accessToken) {

        authenticatedUserChecker.checkAuthenticatedUserExist(userId); // 현재 유저가 존재하는지 체크
        redisTemplate.opsForValue().set(accessToken, "blackList"); // 로그아웃 처리된 토큰을 레디스에 블랙리스트 처리
    }


    public void withdraw(Long userId, String accessToken) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        user.remove();
        redisTemplate.opsForValue().set(accessToken, "blackList");
    }


    public void checkDuplicatedNickname(String nickname) {

        if (userRepository.findUserByNickname(nickname).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }


    public void checkDuplicatedEmail(String email) {

        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public void updatePasswordByForget(ChangePasswordRequest requestDto) {
        String email = requestDto.getEmail();


        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_USER));

        user.updatePassword(requestDto.getPasswordAfter());
    }

    @Transactional
    public void updatePassword(Long userId, ChangePasswordRequest requestDto) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);

        String passwordBefore = passwordEncoder.encode(requestDto.getPasswordBefore());
        String passwordAfter = passwordEncoder.encode(requestDto.getPasswordAfter());

        if (!userRepository.existsByEmailAndPassword(user.getEmail(), passwordBefore)) {
            throw new BusinessException(ErrorCode.BEFORE_PASSWORD_NOT_EXIST);
        }

        user.updatePassword(passwordAfter);
    }


    private User checkEmail(String email) {

        return userRepository.findUserByEmail(email).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.INVALID_EMAIL);
        });
    }

    private void checkPassword(String password, String originPassword) {

        if (!passwordEncoder.matches(password, originPassword)) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
