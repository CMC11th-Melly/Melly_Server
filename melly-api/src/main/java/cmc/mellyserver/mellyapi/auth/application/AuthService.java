package cmc.mellyserver.mellyapi.auth.application;

import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.request.ChangePasswordRequest;
import cmc.mellyserver.mellyapi.auth.application.dto.response.RefreshTokenDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.TokenResponseDto;
import cmc.mellyserver.mellyapi.auth.repository.RefreshToken;
import cmc.mellyserver.mellyapi.auth.repository.RefreshTokenRepository;
import cmc.mellyserver.mellyapi.common.token.TokenProvider;
import cmc.mellyserver.mellycore.comment.application.event.CreateFCMTokenEvent;
import cmc.mellyserver.mellycore.comment.application.event.RemoveFCMTokenEvent;
import cmc.mellyserver.mellycore.comment.application.event.SignupEvent;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static cmc.mellyserver.mellyapi.common.constants.RedisConstants.ACCESS_TOKEN_BLACKLIST;
import static cmc.mellyserver.mellycommon.codes.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final RedisTemplate redisTemplate;

    private final RefreshTokenRepository refreshTokenRepository;

    private final AuthenticatedUserChecker authenticatedUserChecker;

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
        refreshTokenRepository.save(new RefreshToken(refreshToken.getToken(), savedUser.getId()), refreshToken.getExpiredAt()); // 리프레시 토큰 레디스에 저장


        publisher.publishEvent(new CreateFCMTokenEvent(savedUser.getId(), authSignupRequestDto.getFcmToken()));
        publisher.publishEvent(new SignupEvent(savedUser.getId())); // 회원가입 축하 이벤트 발송

        return TokenResponseDto.of(accessToken, refreshToken.getToken());
    }

    /*
    선착순 이벤트 진행 시, 로그인이 몰리는 상황을 가정했을때, TPS를 올릴 수 있는 포인트들을 고민
    1. email 컬럼에 대한 인덱스를 생성해서 DB 랜덤 I/O 시간 단축
    2. password 비교하는 과정에서 encoder의 암호화 강도가 높아서 CPU 사용량과 처리시간 증가, EC2의 CPU 스펙에 맞춰서 암호화 강도 조절
    3. FCM Token을 Redis에 생성하는 부분을 이벤트로 분리. 분산 환경의 Redis를 사용하기에 네트워크 I/O 시간이 추가된다. I/O 시간 단축을 위해서 FCM 관련 로직 이벤트 분리
    4. @TransactionEventListener를 적용해서 데이터 일관성 보장
     */
    @Transactional
    public TokenResponseDto login(AuthLoginRequestDto authLoginRequestDto) {

        User user = checkEmail(authLoginRequestDto.getEmail());
        checkPassword(authLoginRequestDto.getPassword(), user.getPassword());

        user.updateLastLoginTime(LocalDateTime.now()); // 마지막 로그인 시간 업데이트

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoleType());
        RefreshTokenDto refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getRoleType());

        //레디스에 저장 Refresh 토큰을 저장한다. (사용자 Id, refresh 토큰)
        refreshTokenRepository.save(new RefreshToken(refreshToken.getToken(), user.getId()), refreshToken.getExpiredAt());

        publisher.publishEvent(new CreateFCMTokenEvent(user.getId(), authLoginRequestDto.getFcmToken()));

        return TokenResponseDto.of(accessToken, refreshToken.getToken());
    }

    // Refresh Token Rotation (RTR) 전략 적용
    public TokenResponseDto reIssueAccessTokenAndRefreshToken(String token) {

        Long userId = tokenProvider.extractUserId(token);

        RefreshToken refreshToken = refreshTokenRepository.findById(userId).orElseThrow(() -> {
            throw new BusinessException(RE_LOGIN_REQUIRED);
        });

        // 만약 기존의 토큰과 다르다면 비정상적인 접근이 있다 판단하고 재로그인 유도
        if (!refreshToken.getRefreshToken().equals(token)) {
            refreshTokenRepository.remove(userId);
            throw new BusinessException(ABNORMAL_ACCESS);
        }

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(refreshToken.getUserId());

        String newAccessToken = tokenProvider.createAccessToken(refreshToken.getUserId(), user.getRoleType());
        RefreshTokenDto newRefreshToken = tokenProvider.createRefreshToken(refreshToken.getUserId(), user.getRoleType());
        refreshTokenRepository.save(new RefreshToken(newRefreshToken.getToken(), user.getId()), newRefreshToken.getExpiredAt());

        return TokenResponseDto.of(newAccessToken, newRefreshToken.getToken());

    }

    public void logout(Long userId, String accessToken) {

        authenticatedUserChecker.checkAuthenticatedUserExist(userId); // 현재 유저가 존재하는지 체크

        makeAccessTokenDisabled(accessToken); // 로그아웃 처리된 토큰을 레디스에 블랙리스트 처리
        refreshTokenRepository.remove(userId); // 리프레시 토큰 제거
        publisher.publishEvent(new RemoveFCMTokenEvent(userId));
        //  fcmService.deleteToken(userId); // FCM 토큰 제거
    }

    private void makeAccessTokenDisabled(String accessToken) {
        redisTemplate.opsForValue().set(accessToken, ACCESS_TOKEN_BLACKLIST, tokenProvider.getLastExpireTime(accessToken), TimeUnit.MILLISECONDS);
    }


    public void withdraw(Long userId, String accessToken) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        user.remove();

        makeAccessTokenDisabled(accessToken);
        refreshTokenRepository.remove(userId);
        publisher.publishEvent(new RemoveFCMTokenEvent(userId));
        // fcmService.deleteToken(userId);
    }


    public void checkDuplicatedNickname(String nickname) {

        if (userRepository.findUserByNickname(nickname).isPresent()) {
            throw new BusinessException(DUPLICATE_NICKNAME);
        }
    }


    public void checkDuplicatedEmail(String email) {

        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new BusinessException(DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public void updatePasswordByForget(ChangePasswordRequest requestDto) {
        String email = requestDto.getEmail();


        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new BusinessException(NO_SUCH_USER));

        user.updatePassword(requestDto.getPasswordAfter());
    }

    @Transactional
    public void updatePassword(Long userId, ChangePasswordRequest requestDto) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);

        String passwordBefore = passwordEncoder.encode(requestDto.getPasswordBefore());
        String passwordAfter = passwordEncoder.encode(requestDto.getPasswordAfter());

        if (!userRepository.existsByEmailAndPassword(user.getEmail(), passwordBefore)) {
            throw new BusinessException(BEFORE_PASSWORD_NOT_EXIST);
        }

        user.updatePassword(passwordAfter);
    }


    private User checkEmail(String email) {

        return userRepository.findUserByEmail(email).orElseThrow(() -> {
            throw new BusinessException(INVALID_EMAIL);
        });
    }

    private void checkPassword(String password, String originPassword) {

        if (!passwordEncoder.matches(password, originPassword)) {
            throw new BusinessException(INVALID_PASSWORD);
        }
    }

    private void checkDuplicatedEmail(AuthSignupRequestDto authSignupRequestDto) {
        Optional<User> userByEmail = userRepository.findUserByEmail(authSignupRequestDto.getEmail());
        if (userByEmail.isPresent()) {
            throw new BusinessException(DUPLICATE_EMAIL);
        }
    }
}
