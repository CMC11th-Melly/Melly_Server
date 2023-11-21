package cmc.mellyserver.auth.dto.request;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;

public record OAuthSignupRequestDto(
    String email,

    String socialId,

    Provider provider,

    String nickname,

    Gender gender,

    AgeGroup ageGroup,

    String fcmToken
) {

    public User toEntity() {
        return null;
    }

}
