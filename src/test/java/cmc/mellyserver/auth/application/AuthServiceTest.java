package cmc.mellyserver.auth.application;

import cmc.mellyserver.auth.token.JwtTokenProvider;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import cmc.mellyserver.user.domain.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
@Import(JwtTokenProvider.class)
class AuthServiceTest {

//    @InjectMocks
//    private AuthService authService;
//
//    @Mock
//    private UserRepository userRepository;
//
//
//    @Test
//    @DisplayName("일반 로그인 테스트")
//    void normal_login()
//    {
//        BDDMockito.given(userRepository.findUserByEmail(any()))
//                .willReturn(Optional.of(User.builder().uid("cmc11th").email("melly@gmail.com").roleType(RoleType.USER).password("asdfasdf").build()));
//
//        System.out.println("hello");
//        authService.login("melly@gmail.com","asdfasdf");
//    }


}