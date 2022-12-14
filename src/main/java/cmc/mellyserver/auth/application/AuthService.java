package cmc.mellyserver.auth.application;

import cmc.mellyserver.auth.application.dto.AuthRequestForSignupDto;
import cmc.mellyserver.auth.presentation.dto.common.AuthAssembler;
import cmc.mellyserver.auth.presentation.dto.response.AuthResponseForLogin;
import cmc.mellyserver.auth.presentation.dto.response.SignupResponse;
import cmc.mellyserver.auth.token.AuthToken;
import cmc.mellyserver.auth.token.JwtTokenProvider;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
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
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final S3FileLoader s3FileLoader;
    private final AuthenticatedUserChecker authenticatedUserChecker;
    @Value("${app.auth.tokenExpiry}")
    private String expiry;


    public SignupResponse signup(AuthRequestForSignupDto authRequestForSignupDto)
    {
        checkDuplicatedEmail(authRequestForSignupDto.getEmail());
        String filename = s3FileLoader.getMultipartFileName(authRequestForSignupDto.getProfile_image());
        User saveUser = AuthAssembler.createEmailLoginUser(authRequestForSignupDto,passwordEncoder,filename);
        userRepository.save(saveUser);
        AuthToken accessToken = jwtTokenProvider.createToken(saveUser.getUserId(), saveUser.getRoleType(), expiry);
        return AuthAssembler.signupResponse(accessToken.getToken(),saveUser);
    }


    public AuthResponseForLogin login(String email, String password,String fcmToken)
    {
        User user = userRepository.findUserByEmail(email).orElseThrow(()->{throw new GlobalBadRequestException(ExceptionCodeAndDetails.INVALID_EMAIL);});

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.INVALID_PASSWORD);
        }

        user.setFcmToken(fcmToken);
        AuthToken accessToken = jwtTokenProvider.createToken(user.getUserId(), user.getRoleType(), expiry);

        return AuthAssembler.loginResponse(accessToken.getToken(),user);
    }

    public void logout(String uid, String accessToken)
    {
        authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        redisTemplate.opsForValue().set(accessToken,"blackList");
    }


    public void checkDuplicatedEmail(String email)
    {
        Optional<User> member = userRepository.findUserByEmail(email);

        if(member.isPresent()) {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_EMAIL);
        }

    }

    public void checkNicknameDuplicate(String nickname) {
        Optional<User> member = userRepository.findUserByNickname(nickname);

        if(member.isPresent()) {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_NICKNAME);
        }
    }

    /**
     * 1. DB?????? ?????? ??????
     * 2. redis??? ?????? ???????????? ?????? ??????????????? ??????
     */
    public void withdraw(String uid,String accessToken) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        userRepository.delete(user);
        redisTemplate.opsForValue().set(accessToken,"blackList");
    }
}
