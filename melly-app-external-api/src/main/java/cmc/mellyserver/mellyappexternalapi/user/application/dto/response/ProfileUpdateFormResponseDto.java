package cmc.mellyserver.mellyappexternalapi.user.application.dto.response;

import cmc.mellyserver.mellydomain.common.enums.AgeGroup;
import cmc.mellyserver.mellydomain.common.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
