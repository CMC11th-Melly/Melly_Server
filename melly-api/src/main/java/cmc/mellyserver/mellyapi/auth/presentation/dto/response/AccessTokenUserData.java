package cmc.mellyserver.mellyapi.auth.presentation.dto.response;

import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.common.enums.Gender;
import cmc.mellyserver.mellycore.common.enums.Provider;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AccessTokenUserData {

    private Long userSeq;
    private String uid;
    private Provider provider;
    private String email;
    private String nickname;
    private String profileImage;
    private Gender gender;
    private AgeGroup ageGroup;

    public static AccessTokenUserData from(User user) {
        return AccessTokenUserData.builder()
                .userSeq(user.getUserSeq())
                .uid(user.getUserId())
                .provider(user.getProvider())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .gender(user.getGender())
                .ageGroup(user.getAgeGroup())
                .build();

    }
}
