package cmc.mellyserver.auth.application;

import cmc.mellyserver.auth.application.dto.AuthRequestForSignupDto;
import cmc.mellyserver.auth.presentation.dto.AuthAssembler;
import cmc.mellyserver.auth.presentation.dto.LoginResponse;
import cmc.mellyserver.auth.token.AuthToken;
import cmc.mellyserver.auth.token.JwtTokenProvider;
import cmc.mellyserver.common.AWSS3UploadService;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.exception.GlobalServerException;
import cmc.mellyserver.common.exception.MemberNotFoundException;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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
        checkDuplicatedEmail(authRequestForSignupDto.getEmail());
        String filename = getMultipartFileName(authRequestForSignupDto.getProfile_image());
        User saveUser = AuthAssembler.createEmailLoginUser(authRequestForSignupDto,passwordEncoder,filename);
        return userRepository.save(saveUser);
    }


    public LoginResponse login(String email, String password)
    {
        User user = userRepository.findUserByEmail(email).orElseThrow(()->{throw new GlobalBadRequestException(ExceptionCodeAndDetails.INVALID_EMAIL);});

        if(!passwordEncoder.matches(password, user.getPassword()))
        {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.INVALID_PASSWORD);
        }

        AuthToken accessToken = jwtTokenProvider.createToken(user.getUserId(), user.getRoleType(), "99999999999");

        return AuthAssembler.loginResponse(accessToken.getToken(),user);
    }

    public void logout(String userId, String accessToken)
    {
        userRepository.findUserByUserId(userId).orElseThrow(
                ()->{
                    throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
                }
                );

        // 한번 로그아웃한 JWT는 레디스의 블랙리스트 명단에 집어넣음.
        // TODO : 차후 TTL 설정해주기
        redisTemplate.opsForValue().set(accessToken,"blackList");
    }


    public void checkDuplicatedEmail(String email)
    {
        Optional<User> member = userRepository.findUserByEmail(email);

        // case : 회원가입 시도할때 기존 유저가 존재할 때
        if(member.isPresent())
        {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_EMAIL);
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
                    throw new GlobalServerException();
                }
                fileNameList = uploadService.getFileUrl(fileName);

            return fileNameList;
        }
        return null;
    }


    private String createFileName(String fileName) {
        return "user1/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 파일 형식 입니다.");
        }
    }


    public void checkNicknameDuplicate(String nickname) {
        Optional<User> member = userRepository.findUserByNickname(nickname);
        // case : 회원가입 시도할때 기존 유저가 존재할 때
        if(member.isPresent())
        {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_NICKNAME);
        }
    }


    public User getUserData(String userId) {
        return userRepository.findUserByUserId(userId).orElseThrow(()->{throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);});

    }
}
