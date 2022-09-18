package cmc.mellyserver.auth.application.dto;

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

    private Boolean gender;

    @Nullable
    private MultipartFile profile_image;

    @Nullable
    private String birthday;
}
