package cmc.mellyserver.mellycore.user.application.dto.response;

import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.common.enums.Gender;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ProfileUpdateRequestDto {

    private String nickname;

    private Gender gender;

    private AgeGroup ageGroup;


    @Builder
    public ProfileUpdateRequestDto(String nickname, Gender gender, AgeGroup ageGroup) {

        this.nickname = nickname;
        this.gender = gender;
        this.ageGroup = ageGroup;
    }
}
