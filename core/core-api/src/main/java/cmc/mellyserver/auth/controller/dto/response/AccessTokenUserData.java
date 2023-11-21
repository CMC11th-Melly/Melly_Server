package cmc.mellyserver.auth.controller.dto.response;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.Builder;

public record AccessTokenUserData(

    Long id,

    String uid,

    Provider provider,

    String email,

    String nickname,

    String profileImage,

    Gender gender,

    AgeGroup ageGroup

) {

    @Builder
    public AccessTokenUserData {
    }

    public static AccessTokenUserData from(User user) {
        return AccessTokenUserData.builder()
            .id(user.getId())
            .uid(user.getSocialId())
            .provider(user.getProvider())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .profileImage(user.getProfileImage())
            .gender(user.getGender())
            .ageGroup(user.getAgeGroup())
            .build();

    }

}
