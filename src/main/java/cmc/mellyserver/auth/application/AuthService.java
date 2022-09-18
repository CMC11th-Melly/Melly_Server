package cmc.mellyserver.auth.application;

import cmc.mellyserver.auth.application.dto.AuthRequestForSignupDto;
import cmc.mellyserver.auth.exception.DuplicatedUserException;
import cmc.mellyserver.auth.exception.InvalidEmailException;
import cmc.mellyserver.auth.exception.InvalidPasswordException;
import cmc.mellyserver.auth.token.AuthToken;
import cmc.mellyserver.auth.token.JwtTokenProvider;
import cmc.mellyserver.common.AWSS3UploadService;
import cmc.mellyserver.common.exception.MemberNotFoundException;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import cmc.mellyserver.user.domain.RoleType;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final AWSS3UploadService uploadService;

    public User signup(AuthRequestForSignupDto authRequestForSignupDto)
    {
        // 1. 이미 사용자 존재하는지 체크
        checkDuplicatedEmail(authRequestForSignupDto.getEmail());
        String fileName = getMultipartFileName(authRequestForSignupDto.getProfile_image());

        User saveUser = User.builder()
                .email(authRequestForSignupDto.getEmail())
                .password(passwordEncoder.encode(authRequestForSignupDto.getPassword()))
                .roleType(RoleType.USER)
                .profileImage(fileName)
                .gender(authRequestForSignupDto.getGender())
                .userId(UUID.randomUUID().toString())
                .birthday(authRequestForSignupDto.getBirthday())
                .nickname(authRequestForSignupDto.getNickname())
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

    // TODO : 사용자 정보 얻어오는 부분 다시 고민해봐야 함!
    public void logout(String email, String accessToken)
    {
        System.out.println("로그아웃 할때, authentication안에 뭐가 들어있는지 체크! : " + email);
        User user = userRepository.findUserByEmail(email).orElseThrow(MemberNotFoundException::new);
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

    private String getMultipartFileName(MultipartFile file) {

        if(file != null)
        {
            String fileNameList;
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                    uploadService.uploadFile(inputStream,objectMetadata,fileName);
                } catch(IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
                }
                fileNameList = uploadService.getFileUrl(fileName);

            return fileNameList;
        }
        return null;
    }

    // TODO : 차후에 로그인 회원 정보로 prefix 만들면 될 것 같음!
    private String createFileName(String fileName) {
        return "user1/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식 입니다.");
        }
    }


    public Boolean checkNicknameDuplicate(String nickname) {
        Optional<User> member = userRepository.findUserByNickname(nickname);

        // case : 회원가입 시도할때 기존 유저가 존재할 때
        if(member.isPresent())
        {
            return true;
        }
        return false;
    }

    public Boolean checkEmailDuplicate(String email) {
        Optional<User> member = userRepository.findUserByEmail(email);

        // case : 회원가입 시도할때 기존 유저가 존재할 때
        if(member.isPresent())
        {
            return true;
        }
        return false;
    }
}
