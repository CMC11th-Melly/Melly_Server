package cmc.mellyserver.mellycore.user.application.dto.response;

import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.common.enums.Gender;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileUpdateFormResponseDto {

    private String profileImage;
    private String nickname;
    private Gender gender;
    private AgeGroup ageGroup;
}
