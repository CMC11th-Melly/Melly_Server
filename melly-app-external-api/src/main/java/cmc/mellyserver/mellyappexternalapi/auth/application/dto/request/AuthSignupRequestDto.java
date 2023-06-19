package cmc.mellyserver.mellyappexternalapi.auth.application.dto.request;

import cmc.mellyserver.mellydomain.common.enums.AgeGroup;
import cmc.mellyserver.mellydomain.common.enums.Gender;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * AuthSignupDto.java
 *
 * @author jemlog
 */

@Data
public class AuthSignupRequestDto {

    private String email;

    private String password;

    private String nickname;

    private AgeGroup ageGroup;

    private Gender gender;

    private String fcmToken;

    private MultipartFile profile_image;

    @Builder
    public AuthSignupRequestDto(String email, String password, String nickname, AgeGroup ageGroup,
            Gender gender,
            String fcmToken, MultipartFile profile_image) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.fcmToken = fcmToken;
        this.profile_image = profile_image;
    }
}
