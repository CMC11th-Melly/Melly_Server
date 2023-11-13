package cmc.mellyserver.controller.user.dto.request;

import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.domain.user.dto.response.ProfileUpdateRequestDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {

	@Size(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 입력해야 합니다.")
	private String nickname;

	@NotEmpty(message = "성별을 입력해야 합니다.")
	private Gender gender;

	@NotEmpty(message = "연령대를 입력해야 합니다.")
	private AgeGroup ageGroup;

	public ProfileUpdateRequestDto toServiceRequest() {
		return new ProfileUpdateRequestDto(nickname, gender, ageGroup);
	}

}
