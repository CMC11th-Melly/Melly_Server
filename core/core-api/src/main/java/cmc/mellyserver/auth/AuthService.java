package cmc.mellyserver.auth;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.auth.dto.request.AuthLoginRequestDto;
import cmc.mellyserver.auth.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.auth.dto.request.ChangePasswordRequest;
import cmc.mellyserver.auth.dto.response.TokenResponseDto;
import cmc.mellyserver.auth.repository.RefreshToken;
import cmc.mellyserver.auth.token.TokenDto;
import cmc.mellyserver.auth.token.TokenService;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbredis.repository.FcmTokenRepository;
import cmc.mellyserver.domain.comment.event.SignupEvent;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.domain.user.UserWriter;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserReader userReader;

    private final UserWriter userWriter;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final FcmTokenRepository fcmTokenRepository;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public TokenResponseDto signup(AuthSignupRequestDto authSignupRequestDto) {

        checkDuplicatedEmail(authSignupRequestDto.getEmail());
        User user = userWriter.save(authSignupRequestDto.toEntity());
        System.out.println(user.getPassword());
        TokenDto tokenDto = tokenService.createToken(user);

        fcmTokenRepository.saveToken(user.getId().toString(), user.getFcmToken());
        publisher.publishEvent(new SignupEvent(user.getId()));

        return TokenResponseDto.of(tokenDto.accessToken(), tokenDto.refreshToken().getToken());
    }

    /*
     로그인 요청이 몰리는 상황에서 TPS를 올릴 수 있는 방법들을 고민했습니다.
      - 1. email 컬럼에 대한 인덱스를 생성해서 DB 랜덤 I/O 시간 단축
      - 2. password 비교하는 과정에서 encoder의 암호화 강도가 높아서 CPU 사용량과 처리시간 증가, EC2의 CPU 스펙에 맞춰서 암호화 강도 조절
     */
    @Transactional
    public TokenResponseDto login(AuthLoginRequestDto authLoginRequestDto) {

        User user = checkEmail(authLoginRequestDto.getEmail());
        checkPassword(authLoginRequestDto.getPassword(), user.getPassword());

        TokenDto tokenDto = tokenService.createToken(user);
        fcmTokenRepository.saveToken(user.getId().toString(), authLoginRequestDto.getFcmToken());

        return TokenResponseDto.of(tokenDto.accessToken(), tokenDto.refreshToken().getToken());
    }

    // Refresh Token Rotation (RTR) 전략 적용
    public TokenResponseDto reIssueAccessTokenAndRefreshToken(final String token) {

        Long userId = tokenService.extractUserId(token);
        RefreshToken refreshToken = tokenService.findRefreshToken(userId);

        checkAbnormalUserAccess(token, userId, refreshToken);

        User user = userReader.findById(refreshToken.getUserId());
        TokenDto tokenDto = tokenService.createToken(user);

        return TokenResponseDto.of(tokenDto.accessToken(), tokenDto.refreshToken().getToken());
    }

    public void logout(final Long userId, final String accessToken) {

        tokenService.makeAccessTokenDisabled(accessToken);
        tokenService.removeRefreshToken(userId);
        fcmTokenRepository.deleteToken(userId.toString());
    }

    public void withdraw(final Long userId, final String accessToken) {

        User user = userReader.findById(userId);
        user.remove();

        tokenService.makeAccessTokenDisabled(accessToken);
        tokenService.removeRefreshToken(userId);
        fcmTokenRepository.deleteToken(userId.toString());
    }

    public void checkDuplicatedNickname(final String nickname) {

        if (userReader.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    public void checkDuplicatedEmail(final String email) {

        if (userReader.findByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public void updateForgetPassword(ChangePasswordRequest requestDto) {

        User user = userReader.findByEmail(requestDto.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.changePassword(requestDto.getPasswordAfter());
    }

    @Transactional
    public void changePassword(final Long userId, ChangePasswordRequest requestDto) {

        User user = userReader.findById(userId);

        String passwordBefore = passwordEncoder.encode(requestDto.getPasswordBefore());
        String passwordAfter = passwordEncoder.encode(requestDto.getPasswordAfter());

        if (!userReader.existsByEmailAndPassword(user.getEmail(), passwordBefore)) {
            throw new BusinessException(ErrorCode.BEFORE_PASSWORD_NOT_EXIST);
        }

        user.changePassword(passwordAfter);
    }

    private User checkEmail(final String email) {
        return userReader.findByEmail(email).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.INVALID_EMAIL);
        });
    }

    private void checkPassword(final String password, final String originPassword) {
        if (!passwordEncoder.matches(password, originPassword)) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private void checkAbnormalUserAccess(final String token, final Long userId, final RefreshToken refreshToken) {

        if (!refreshToken.getRefreshToken().equals(token)) {
            tokenService.removeRefreshToken(userId);
            throw new BusinessException(ErrorCode.ABNORMAL_ACCESS);
        }
    }

}
