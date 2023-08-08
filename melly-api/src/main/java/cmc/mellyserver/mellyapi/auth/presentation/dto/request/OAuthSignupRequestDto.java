package cmc.mellyserver.mellyapi.auth.presentation.dto.request;

import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.common.enums.Gender;
import cmc.mellyserver.mellycore.common.enums.Provider;
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
