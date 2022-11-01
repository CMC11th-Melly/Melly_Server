package cmc.mellyserver.user.presentation.dto.request;

import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ProfileUpdateRequest {

    private MultipartFile profileImage;
    private String nickname;
    private Gender gender;
    private AgeGroup ageGroup;
}
