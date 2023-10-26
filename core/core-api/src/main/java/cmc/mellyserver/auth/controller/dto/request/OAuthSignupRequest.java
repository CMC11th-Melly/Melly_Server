package cmc.mellyserver.auth.controller.dto.request;

import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthSignupRequest {

	// Oauth를 통해 넘겨받은 정보
	private String email;

	private String socialId;

	private Provider provider;

	// 입력 정보
	@Length(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 작성해주세요.")
	private String nickname; // 닉네임

	private Gender gender; // 성별

	private AgeGroup ageGroup; // 연령대

	private String fcmToken;

	public OAuthSignupRequestDto toDto() {
		return new OAuthSignupRequestDto(email, socialId, provider, nickname, gender, ageGroup, fcmToken);
	}

}
