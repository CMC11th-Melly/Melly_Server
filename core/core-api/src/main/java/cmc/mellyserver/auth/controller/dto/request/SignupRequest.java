package cmc.mellyserver.auth.controller.dto.request;

import org.hibernate.validator.constraints.Length;

import cmc.mellyserver.auth.service.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(
    @Email(message = "이메일 양식으로 작성해주세요.")
    String email,

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}", message = "비밀번호는 영문과 숫자 조합으로 8자리 이상 가능합니다.")
    String password,

    @Length(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 작성해주세요.")
    String nickname,

    Gender gender,

    AgeGroup ageGroup,

    String fcmToken
) {

    public AuthSignupRequestDto toDto() {
        return AuthSignupRequestDto.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .ageGroup(ageGroup)
            .gender(gender)
            .fcmToken(fcmToken)
            .build();
    }

}
