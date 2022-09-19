package cmc.mellyserver.auth.application.dto;

import cmc.mellyserver.user.domain.AgeGroup;
import io.swagger.v3.oas.annotations.media.Schema;
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

    private Boolean gender;

    @Nullable
    private MultipartFile profile_image;

}
