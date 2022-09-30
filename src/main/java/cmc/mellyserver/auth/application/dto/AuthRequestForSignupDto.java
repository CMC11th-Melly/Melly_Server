package cmc.mellyserver.auth.application.dto;

import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
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

    @Nullable
    private MultipartFile profile_image;

}
