package cmc.mellyserver.mellyapi.auth.presentation.dto.request;

import cmc.mellyserver.mellycommon.enums.AgeGroup;
import cmc.mellyserver.mellycommon.enums.Gender;
import cmc.mellyserver.mellycommon.enums.Provider;
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
