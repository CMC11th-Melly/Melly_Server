package cmc.mellyserver.mellyapi.auth.presentation.dto.response;

import cmc.mellyserver.mellycommon.enums.AgeGroup;
import cmc.mellyserver.mellycommon.enums.Gender;
import cmc.mellyserver.mellycommon.enums.Provider;
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

    private Long id;
    private String uid;
    private Provider provider;
    private String email;
    private String nickname;
    private String profileImage;
    private Gender gender;
    private AgeGroup ageGroup;

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
