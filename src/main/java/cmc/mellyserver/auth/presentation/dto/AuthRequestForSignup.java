package cmc.mellyserver.auth.presentation.dto;

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

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestForSignup {

    @Schema(example = "melly@gmail.com")
    private String email;

    @Schema(example = "cmc11th")
    @Length(min = 8)
    private String password;

    @Schema(example = "모카")
    private String nickname;

    @Schema(example = "남성는 MALE, 여성는 FEMALE")
    private Gender gender;

    @Schema(example = "프로필 이미지 사진")
    @Nullable
    private MultipartFile profileImage;

    @Schema(example = "ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,DEFAULT")
    @Nullable
    private AgeGroup ageGroup;

    @Builder
    public AuthRequestForSignup(String email, String password, String nickname, Gender gender, @Nullable MultipartFile profileImage, @Nullable AgeGroup ageGroup) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
    }
}
