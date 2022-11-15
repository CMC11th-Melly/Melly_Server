package cmc.mellyserver.auth.presentation.dto.request;

import cmc.mellyserver.common.constraint.NoEmoji;
import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestForSignup {

    @Schema(example = "melly@gmail.com")
    @Email(message = "이메일 형식이 올바르지 않습니다.")     // ok
    private String email;

    @Schema(example = "cmc11th")
    @Length(min = 8,message = "비밀번호는 8자 이상 입력해주세요.")  // ok
    private String password;

    @Schema(example = "모카")
    @Length(max = 8,message = "닉네임은 8자 이하로 입력해주세요.")  // ok
    @Length(min = 2,message = "닉네임은 2자 이상 입력해주세요.")   // ok
    @NotBlank(message = "닉네임에 공백은 불가합니다.")    // ok
    @NoEmoji     // ok
    private String nickname;

    @Schema(example = "남성는 MALE, 여성는 FEMALE")
    private Gender gender;

    @Schema(example = "프로필 이미지 사진")
    @Nullable
    private MultipartFile profileImage;

    @Schema(example = "ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,DEFAULT")
    @Nullable
    private AgeGroup ageGroup;

    @Schema(description = "유저 디바이스 fcm 토큰")
    private String fcmToken;

    @Builder
    public AuthRequestForSignup(String email, String password, String nickname, Gender gender, @Nullable MultipartFile profileImage, @Nullable AgeGroup ageGroup,String fcmToken) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
        this.fcmToken = fcmToken;
    }
}
