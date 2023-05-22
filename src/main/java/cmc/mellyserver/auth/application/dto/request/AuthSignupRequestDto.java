package cmc.mellyserver.auth.application.dto.request;

import cmc.mellyserver.common.enums.AgeGroup;
import cmc.mellyserver.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * AuthSignupDto.java
 *
 * @author jemlog
 */

@Data
@AllArgsConstructor
public class AuthSignupRequestDto {

    private String email;

    private String password;

    private String nickname;

    private AgeGroup ageGroup;

    private Gender gender;

    private String fcmToken;

    private MultipartFile profile_image;
}
