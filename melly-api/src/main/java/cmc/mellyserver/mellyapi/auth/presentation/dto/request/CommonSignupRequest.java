package cmc.mellyserver.mellyapi.auth.presentation.dto.request;

import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.common.enums.Gender;
import cmc.mellyserver.mellycore.common.enums.Provider;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonSignupRequest {

    private String email;
    private String uid;
    private String nickname;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}",
            message = "비밀번호는 영문과 숫자 조합으로 8자리 이상 가능합니다.")
    @Nullable
    private String password;

    private Provider provider;

    private Gender gender;

    @Nullable
    private MultipartFile profileImage;

    @Nullable
    private AgeGroup ageGroup;

    @Nullable
    private String fcmToken;

}
