package cmc.mellyserver.auth.controller.dto.request;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestForSignup {

    @Email(message = "이메일 형식이 올바르지 않습니다.") // ok
    private String email;

    @Size(min = 8, message = "비밀번호는 8자 이상 입력해주세요.") // ok
    private String password;

    @Size(min = 2, max = 8, message = "닉네임은 2자 이상 8자 이하로 입력해주세요.") // ok
    private String nickname;

    private Gender gender;

    @Nullable
    private MultipartFile profileImage;

    @Nullable
    private AgeGroup ageGroup;

    private String fcmToken;

    @Builder
    public AuthRequestForSignup(String email, String password, String nickname, Gender gender,
        @Nullable MultipartFile profileImage, @Nullable AgeGroup ageGroup, String fcmToken) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
        this.fcmToken = fcmToken;
    }

}
