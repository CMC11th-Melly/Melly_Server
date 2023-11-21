package cmc.mellyserver.domain.user.dto.request;

import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import lombok.Builder;

public record ProfileUpdateRequestDto(String nickname, Gender gender, AgeGroup ageGroup) {

    @Builder
    public ProfileUpdateRequestDto {
    }

}
