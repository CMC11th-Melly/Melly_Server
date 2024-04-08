package cmc.mellyserver.auth.service;

import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.auth.common.event.SignupEvent;
import cmc.mellyserver.auth.controller.dto.request.ChangePasswordRequest;
import cmc.mellyserver.auth.service.dto.request.AuthLoginRequestDto;
import cmc.mellyserver.auth.service.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.auth.service.dto.response.TokenResponseDto;
import cmc.mellyserver.auth.token.RefreshToken;
import cmc.mellyserver.auth.token.TokenDto;
import cmc.mellyserver.auth.token.TokenService;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.domain.user.UserWriter;
import cmc.mellyserver.support.exception.CommonException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserReader userReader;

    private final UserWriter userWriter;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final NotificationTokenDao notificationTokenDao;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public TokenResponseDto signup(AuthSignupRequestDto authSignupRequestDto) {

        checkDuplicatedEmail(authSignupRequestDto.email());
        User user = userWriter.save(authSignupRequestDto.toEntity());
        TokenDto tokenDto = tokenService.createToken(user);

        notificationTokenDao.save(user.getId().toString(), user.getFcmToken());
        publisher.publishEvent(new SignupEvent(user.getId()));

        return TokenResponseDto.of(tokenDto.accessToken(), tokenDto.refreshToken().token());
    }

    @Transactional
    public TokenResponseDto login(AuthLoginRequestDto authLoginRequestDto) {

        User user = checkEmail(authLoginRequestDto.email());
        checkPassword(authLoginRequestDto.password(), user.getPassword());

        TokenDto tokenDto = tokenService.createToken(user);
        notificationTokenDao.save(user.getId().toString(), authLoginRequestDto.fcmToken());

        return TokenResponseDto.of(tokenDto.accessToken(), tokenDto.refreshToken().token());
    }

    public TokenResponseDto reIssueAccessTokenAndRefreshToken(final String token) {

        Long userId = tokenService.extractUserId(token);
        RefreshToken refreshToken = tokenService.findRefreshToken(userId);

        checkAbnormalUserAccess(token, userId, refreshToken);

        User user = userReader.findById(refreshToken.userId());
        TokenDto tokenDto = tokenService.createToken(user);

        return TokenResponseDto.of(tokenDto.accessToken(), tokenDto.refreshToken().token());
    }

    public void logout(final Long userId, final String accessToken) {

        tokenService.makeAccessTokenDisabled(accessToken);
        tokenService.removeRefreshToken(userId);
        notificationTokenDao.remove(userId.toString());
    }

    public void withdraw(final Long userId, final String accessToken) {

        User user = userReader.findById(userId);
        user.remove();

        tokenService.makeAccessTokenDisabled(accessToken);
        tokenService.removeRefreshToken(userId);
        notificationTokenDao.remove(userId.toString());
    }

    public void checkDuplicatedNickname(final String nickname) {

        if (userReader.existsByNickname(nickname)) {
            throw new CommonException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    public void checkDuplicatedEmail(final String email) {

        if (Objects.nonNull(userReader.findByEmail(email))) {
            throw new CommonException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public void updateForgetPassword(ChangePasswordRequest requestDto) {

        User user = userReader.findByEmail(requestDto.email());

        if (Objects.isNull(user)) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        user.changePassword(requestDto.passwordAfter());
    }

    @Transactional
    public void changePassword(final Long userId, ChangePasswordRequest requestDto) {

        User user = userReader.findById(userId);

        String passwordBefore = passwordEncoder.encode(requestDto.passwordBefore());
        String passwordAfter = passwordEncoder.encode(requestDto.passwordAfter());

        if (!userReader.existsByEmailAndPassword(user.getEmail(), passwordBefore)) {
            throw new CommonException(ErrorCode.BEFORE_PASSWORD_NOT_EXIST);
        }

        user.changePassword(passwordAfter);
    }

    private User checkEmail(final String email) {
        User byEmail = userReader.findByEmail(email);

        if (Objects.isNull(byEmail)) {
            throw new CommonException(ErrorCode.INVALID_EMAIL);
        }

        return byEmail;
    }

    private void checkPassword(final String password, final String originPassword) {
        if (!passwordEncoder.matches(password, originPassword)) {
            throw new CommonException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private void checkAbnormalUserAccess(final String token, final Long userId, final RefreshToken refreshToken) {

        if (!refreshToken.refreshToken().equals(token)) {
            tokenService.removeRefreshToken(userId);
            throw new CommonException(ErrorCode.ABNORMAL_ACCESS);
        }
    }

}
