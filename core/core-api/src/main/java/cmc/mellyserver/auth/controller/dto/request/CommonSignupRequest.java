package cmc.mellyserver.auth.controller.dto.request;

import org.hibernate.validator.constraints.Length;

import cmc.mellyserver.auth.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonSignupRequest {

    @Email(message = "이메일 양식으로 작성해주세요.")
    private String email; // 이메일

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}", message = "비밀번호는 영문과 숫자 조합으로 8자리 이상 가능합니다.")
    private String password; // 비밀번호

    @Length(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 작성해주세요.")
    private String nickname; // 닉네임

    private Gender gender; // 성별

    private AgeGroup ageGroup; // 연령대

    private String fcmToken;

    public AuthSignupRequestDto toDto() {
        return new AuthSignupRequestDto(email, password, nickname, ageGroup, gender, fcmToken);
    }

}
