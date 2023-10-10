package cmc.mellyserver.auth.controller.dto.request;


import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthSignupRequestDto {

    private String email;

    private String socialId;

    private Provider provider;

    private String nickname;


    private Gender gender;


    private AgeGroup ageGroup;

    private String fcmToken;
}
