package cmc.mellyserver.auth.application.dto;

import cmc.mellyserver.common.enums.AgeGroup;
import cmc.mellyserver.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class AuthRequestForSignupDto {

    private String email;

    private String password;

    private String nickname;

    private AgeGroup ageGroup;

    private Gender gender;

    private String fcmToken;

    @Nullable
    private MultipartFile profile_image;

}
