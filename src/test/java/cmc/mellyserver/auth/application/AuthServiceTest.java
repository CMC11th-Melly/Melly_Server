package cmc.mellyserver.auth.application;

import cmc.mellyserver.common.token.JwtTokenProvider;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.aws.AWSS3UploadService;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RedisTemplate redisTemplate;
    @Mock
    private AWSS3UploadService uploadService;

    @InjectMocks
    private AuthService authService;


    @Test
    @DisplayName("이메일이나 비밀번호가 다르면 예외가 발생한다.")
    void checkLogin()
    {
        given(userRepository.findUserByEmail(any())).willReturn(Optional.empty());

        Assertions.assertThatThrownBy(()->{
            authService.login("melly@gmail.com","asdfasdf","fafegesgseg");
        }).isInstanceOf(GlobalBadRequestException.class);
    }

    @Test
    @DisplayName("회원가입 시에 닉네임 중복이 있는지 체크한다")
    void checkDuplicatedNickname()
    {
        given(userRepository.findUserByNickname(any())).willReturn(Optional.of(User.builder().build()));

        Assertions.assertThatThrownBy(()->{
            authService.checkNicknameDuplicate("nickname");
        }).isInstanceOf(GlobalBadRequestException.class);
    }

    @Test
    @DisplayName("회원가입 시에 이메일 중복이 있는지 체크한다.")
    void checkDuplicatedEmail()
    {
        given(userRepository.findUserByEmail(any())).willReturn(Optional.of(User.builder().build()));

        Assertions.assertThatThrownBy(()->{
            authService.checkDuplicatedEmail("email");
        }).isInstanceOf(GlobalBadRequestException.class);
    }


}