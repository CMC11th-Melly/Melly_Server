package cmc.mellyserver.domain.auth;

import cmc.mellyserver.common.token.TokenProvider;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.auth.dto.request.AuthLoginRequestDto;
import cmc.mellyserver.domain.auth.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.domain.auth.dto.request.ChangePasswordRequest;
import cmc.mellyserver.domain.auth.dto.response.RefreshTokenDto;
import cmc.mellyserver.domain.auth.dto.response.TokenResponseDto;
import cmc.mellyserver.domain.auth.repository.JWTRepository;
import cmc.mellyserver.domain.auth.repository.RefreshToken;
import cmc.mellyserver.domain.comment.event.SignupEvent;
import cmc.mellyserver.notification.fcm.FCMTokenManageService;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

    private final TokenProvider tokenProvider;

    private final JWTRepository tokenRepository;

    private final FCMTokenManageService tokenManageService;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public TokenResponseDto emailSignup(AuthSignupRequestDto authSignupRequestDto) {

        checkDuplicatedEmail(authSignupRequestDto);
        String encodedPassword = passwordEncoder.encode(authSignupRequestDto.getPassword()); // 비밀번호 암호화 -> 암호화 강도 낮춰서 CPU 성능 확보

        User savedUser = userRepository.save(User.createEmailLoginUser(authSignupRequestDto.getEmail(), encodedPassword, authSignupRequestDto.getNickname(),
                authSignupRequestDto.getAgeGroup(),
                authSignupRequestDto.getGender()));
        savedUser.updateLastLoginTime(LocalDateTime.now()); // 회원가입 후, 바로 로그인이기 때문에 마지막 로그인 시간 업데이트

        String accessToken = tokenProvider.createAccessToken(savedUser.getId(), savedUser.getRoleType()); // 액세스 토큰 발행
        RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(savedUser.getId(), savedUser.getRoleType()); // 리프레시 토큰 발행

        tokenRepository.saveRefreshToken(new RefreshToken(refreshToken.getToken(), savedUser.getId()), refreshToken.getExpiredAt()); // 리프레시 토큰 레디스에 저장
        tokenManageService.saveToken(savedUser.getId(), authSignupRequestDto.getFcmToken());

        publisher.publishEvent(new SignupEvent(savedUser.getId())); // 회원가입 축하 이벤트 발송

        return TokenResponseDto.of(accessToken, refreshToken.getToken());
    }

    /**
     * 로그인 요청이 몰리는 상황에서 TPS를 올릴 수 있는 방법들을 고민했습니다.
     * <p>
     * 1. email 컬럼에 대한 인덱스를 생성해서 DB 랜덤 I/O 시간 단축
     * <p>
     * 2. password 비교하는 과정에서 encoder의 암호화 강도가 높아서 CPU 사용량과 처리시간 증가, EC2의 CPU 스펙에 맞춰서 암호화 강도 조절
     * <p>
     * 3. FCM Token을 Redis에 생성하는 부분을 이벤트로 분리. 분산 환경의 Redis를 사용하기에 네트워크 I/O 시간이 추가됩니다. I/O 시간 단축을 위해서 FCM 관련 로직 이벤트 분리
     * <p>
     * 4. @TransactionEventListener를 적용해서 데이터 일관성 보장
     */
    @Transactional
    public TokenResponseDto login(AuthLoginRequestDto authLoginRequestDto) {

        User user = checkEmail(authLoginRequestDto.getEmail());
        checkPassword(authLoginRequestDto.getPassword(), user.getPassword());
        user.updateLastLoginTime(LocalDateTime.now());

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
        RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());

        // ------ 3. 레디스에 Refresh token 저장, EC2 간 네트워크 I/O 발생
        tokenRepository.saveRefreshToken(new RefreshToken(refreshToken.getToken(), user.getId()), refreshToken.getExpiredAt());
        tokenManageService.saveToken(user.getId(), authLoginRequestDto.getFcmToken());

        return TokenResponseDto.of(accessToken, refreshToken.getToken());
    }


    // Refresh Token Rotation (RTR) 전략 적용
    public TokenResponseDto reIssueAccessTokenAndRefreshToken(final String token) {

        Long userId = tokenProvider.extractUserId(token);
        RefreshToken refreshToken = tokenRepository.findRefreshToken(userId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.RELOGIN_REQUIRED);
        });

        // 만약 기존의 토큰과 다르다면 비정상적인 접근이 있다 판단하고 재로그인 유도
        checkAbnormalUserAccess(token, userId, refreshToken);

        User user = userRepository.getById(refreshToken.getUserId());

        String newAccessToken = tokenProvider.createAccessToken(refreshToken.getUserId(), user.getRoleType());
        RefreshTokenDto newRefreshToken = tokenProvider.createRefreshToken(refreshToken.getUserId(), user.getRoleType());
        tokenRepository.saveRefreshToken(new RefreshToken(newRefreshToken.getToken(), user.getId()), newRefreshToken.getExpiredAt());

        return TokenResponseDto.of(newAccessToken, newRefreshToken.getToken());
    }


    public void logout(final Long userId, final String accessToken) {

        tokenRepository.makeAccessTokenDisabled(accessToken);
        tokenRepository.removeRefreshToken(userId);

        tokenManageService.deleteToken(userId);
    }


    public void withdraw(final Long userId, final String accessToken) {

        User user = userRepository.getById(userId);
        user.remove();

        tokenRepository.makeAccessTokenDisabled(accessToken);
        tokenRepository.removeRefreshToken(userId);

        tokenManageService.deleteToken(userId);
    }


    public void checkDuplicatedNickname(final String nickname) {

        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }


    public void checkDuplicatedEmail(final String email) {

        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public void updateForgetPassword(ChangePasswordRequest requestDto) {

        User user = userRepository.findUserByEmail(requestDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.changePassword(requestDto.getPasswordAfter());
    }

    @Transactional
    public void changePassword(final Long userId, ChangePasswordRequest requestDto) {

        User user = userRepository.getById(userId);

        String passwordBefore = passwordEncoder.encode(requestDto.getPasswordBefore());
        String passwordAfter = passwordEncoder.encode(requestDto.getPasswordAfter());

        if (!userRepository.existsByEmailAndPassword(user.getEmail(), passwordBefore)) {
            throw new BusinessException(ErrorCode.BEFORE_PASSWORD_NOT_EXIST);
        }

        user.changePassword(passwordAfter);
    }


    private User checkEmail(final String email) {

        return userRepository.findUserByEmail(email).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.INVALID_EMAIL);
        });
    }


    private void checkPassword(final String password, final String originPassword) {

        if (!passwordEncoder.matches(password, originPassword)) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }


    private void checkDuplicatedEmail(AuthSignupRequestDto authSignupRequestDto) {

        if (userRepository.existsByEmail(authSignupRequestDto.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }


    private void checkAbnormalUserAccess(final String token, final Long userId, RefreshToken refreshToken) {
        if (!refreshToken.getRefreshToken().equals(token)) {
            tokenRepository.removeRefreshToken(userId);
            throw new BusinessException(ErrorCode.ABNORMAL_ACCESS);
        }
    }
}
