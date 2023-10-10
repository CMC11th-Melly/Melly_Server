package cmc.mellyserver.auth.controller.dto.response;


import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;
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
