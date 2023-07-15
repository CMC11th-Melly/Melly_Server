package cmc.mellyserver.mellyapi.user.presentation.dto.response;

import cmc.mellyserver.mellycommon.enums.AgeGroup;
import cmc.mellyserver.mellycommon.enums.Gender;
import lombok.Builder;
import lombok.Data;

@Data
public class ProfileUpdateFormResponse {

    private String profileImage;

    private String nickname;

    private Gender gender;

    private AgeGroup ageGroup;

    @Builder
    public ProfileUpdateFormResponse(String profileImage, String nickname, Gender gender,
            AgeGroup ageGroup) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.gender = gender;
        this.ageGroup = ageGroup;
    }
}