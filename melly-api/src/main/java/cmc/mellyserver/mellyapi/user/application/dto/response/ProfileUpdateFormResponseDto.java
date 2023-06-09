package cmc.mellyserver.mellyapi.user.application.dto.response;

import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProfileUpdateFormResponseDto {

    private String profileImage;
    private String nickname;
    private Gender gender;
    private AgeGroup ageGroup;
}
