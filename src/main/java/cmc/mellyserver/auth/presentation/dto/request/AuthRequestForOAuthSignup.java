package cmc.mellyserver.auth.presentation.dto.request;

import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestForOAuthSignup {

    @Schema(example = "소셜 아이디")
    private String uid;

    @Schema(example = "모카")
    private String nickname;

    @Schema(example = "남성는 true, 여성는 false")
    private Gender gender;

    @Schema(example = "프로필 이미지 사진")
    @Nullable
    private MultipartFile profileImage;

    @Schema(example = "TWO")
    @Nullable
    private AgeGroup ageGroup;

    @Nullable
    @Schema(description = "인증 유저 디바이스 fcm token")
    private String fcmToken;
}
