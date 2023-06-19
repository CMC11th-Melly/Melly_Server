package cmc.mellyserver.mellyappexternalapi.user.presentation.dto.request;

import cmc.mellyserver.mellydomain.common.enums.AgeGroup;
import cmc.mellyserver.mellydomain.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
public class ProfileUpdateRequest {

    private MultipartFile profileImage;

    private String nickname;

    private Gender gender;

    private AgeGroup ageGroup;

    private boolean deleteImage;

}
