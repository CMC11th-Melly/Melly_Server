package cmc.mellyserver.user.application.dto;

import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileUpdateFormResponse {

    private String profileImage;
    private String nickname;
    private Gender gender;
    private AgeGroup ageGroup;
}
