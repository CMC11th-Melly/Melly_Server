package cmc.mellyserver.auth.application.impl;

import cmc.mellyserver.auth.application.AuthService;
import cmc.mellyserver.auth.application.dto.request.AuthLoginRequestDto;
import cmc.mellyserver.auth.application.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.auth.application.dto.response.LoginResponseDto;
import cmc.mellyserver.auth.application.dto.response.SignupResponseDto;
import cmc.mellyserver.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.auth.presentation.dto.response.AuthResponseForLogin;
import cmc.mellyserver.auth.presentation.dto.response.SignupResponse;
import cmc.mellyserver.common.token.AuthToken;
import cmc.mellyserver.common.token.JwtTokenProvider;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.repository.UserRepository;
import com.google.j2objc.annotations.ObjectiveCName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final S3FileLoader s3FileLoader;
    private final AuthenticatedUserChecker authenticatedUserChecker;

    @Value("${app.auth.tokenExpiry}")
    private String expiry;


    @Override
    public SignupResponseDto signup(AuthSignupRequestDto authSignupRequestDto)
    {
        checkDuplicatedEmail(authSignupRequestDto.getEmail());

        String filename = s3FileLoader.getMultipartFileName(authSignupRequestDto.getProfile_image());
        User savedUser = userRepository.save(AuthAssembler.createEmailLoginUser(authSignupRequestDto, passwordEncoder, filename));
        String token = jwtTokenProvider.createToken(savedUser.getUserSeq(), savedUser.getRoleType());
        return SignupResponseDto.of(savedUser,token);
    }


    @Override
    public LoginResponseDto login(AuthLoginRequestDto authLoginRequestDto)
    {
        User user = userRepository.findUserByEmail(authLoginRequestDto.getEmail()).orElseThrow(()->{throw new GlobalBadRequestException(ExceptionCodeAndDetails.INVALID_EMAIL);});

        checkPassword(authLoginRequestDto.getPassword(), user);

        user.setFcmToken(authLoginRequestDto.getFcmToken());
        String token = jwtTokenProvider.createToken(user.getUserSeq(), user.getRoleType());
        return LoginResponseDto.of(user,token);
    }


    @Override
    public void logout(Long userSeq, String accessToken)
    {
        authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        redisTemplate.opsForValue().set(accessToken,"blackList");
    }


    @Override
    public void withdraw(Long userSeq,String accessToken) {
        userRepository.delete(authenticatedUserChecker.checkAuthenticatedUserExist(userSeq));
        redisTemplate.opsForValue().set(accessToken,"blackList");
    }


    @Override
    public void checkDuplicatedNickname(String nickname) {
        Optional<User> member = userRepository.findUserByNickname(nickname);
        if(member.isPresent()) {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_NICKNAME);
        }
    }


    @Override
    public void checkDuplicatedEmail(String email)
    {
        Optional<User> member = userRepository.findUserByEmail(email);

        if(member.isPresent()) {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_EMAIL);
        }
    }


    private void checkPassword(String password, User user) {
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.INVALID_PASSWORD);
        }
    }
}
