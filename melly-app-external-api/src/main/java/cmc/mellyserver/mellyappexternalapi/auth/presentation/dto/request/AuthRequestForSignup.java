package cmc.mellyserver.mellyappexternalapi.auth.presentation.dto.request;

import cmc.mellyserver.mellyappexternalapi.common.constraint.NoEmoji;
import cmc.mellyserver.mellydomain.common.enums.AgeGroup;
import cmc.mellyserver.mellydomain.common.enums.Gender;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestForSignup {

    @Email(message = "이메일 형식이 올바르지 않습니다.")     // ok
    private String email;

    @Length(min = 8, message = "비밀번호는 8자 이상 입력해주세요.")  // ok
    private String password;

    @Length(max = 8, message = "닉네임은 8자 이하로 입력해주세요.")  // ok
    @Length(min = 2, message = "닉네임은 2자 이상 입력해주세요.")   // ok
    @NotBlank(message = "닉네임에 공백은 불가합니다.")    // ok
    @NoEmoji     // ok
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
