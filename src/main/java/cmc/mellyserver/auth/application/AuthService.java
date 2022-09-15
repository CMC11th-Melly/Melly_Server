package cmc.mellyserver.auth.application;

import cmc.mellyserver.auth.exception.DuplicatedUserException;
import cmc.mellyserver.auth.exception.InvalidEmailException;
import cmc.mellyserver.auth.exception.InvalidPasswordException;
import cmc.mellyserver.auth.token.AuthToken;
import cmc.mellyserver.auth.token.JwtTokenProvider;
import cmc.mellyserver.common.exception.MemberNotFoundException;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import cmc.mellyserver.user.domain.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    public User signup(String email, String password)
    {
        // 1. 이미 사용자 존재하는지 체크
        checkDuplicatedEmail(email);
        // 2. 존재하지 않는 사용자라면 비밀번호 암호화해서 저장
        User saveUser = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roleType(RoleType.USER)
                .build();
        // 3. 저장한 Member 반환
        return userRepository.save(saveUser);
    }

    public String login(String email, String password)
    {
        // 1. 입력한 이메일로 조회 안되면 invalidEmail 예외 반환
        User user = userRepository.findUserByEmail(email).orElseThrow(InvalidEmailException::new);
        // 2. 비밀 번호가 일치하지 않는다면
        if(!passwordEncoder.matches(password, user.getPassword()))
        {
            throw new InvalidPasswordException("일치하지 않는 비밀번호입니다.");
        }
        AuthToken accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRoleType(), "1000000");
        return accessToken.getToken();
    }

    public void logout(String accessToken)
    {
        User user = userRepository.findById(1L).orElseThrow(MemberNotFoundException::new);
        userRepository.delete(user);
        redisTemplate.opsForValue().set(accessToken,"blackList");
    }


    private void checkDuplicatedEmail(String email)
    {
        Optional<User> member = userRepository.findUserByEmail(email);

        // case : 회원가입 시도할때 기존 유저가 존재할 때
        if(member.isPresent())
        {
            throw new DuplicatedUserException("이미 존재하는 사용자입니다.");
        }
    }

}
