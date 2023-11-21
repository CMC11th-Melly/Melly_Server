package cmc.mellyserver.auth.controller.dto.request;

import org.hibernate.validator.constraints.Length;

import cmc.mellyserver.auth.dto.request.OAuthSignupRequestDto;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;

public record OAuthSignupRequest(
    String email,

    String socialId,

    Provider provider,

    @Length(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 작성해주세요.")
    String nickname,

    Gender gender,

    AgeGroup ageGroup,

    String fcmToken
) {

    public OAuthSignupRequestDto toDto() {
        return new OAuthSignupRequestDto(email, socialId, provider, nickname, gender, ageGroup, fcmToken);
    }

}
