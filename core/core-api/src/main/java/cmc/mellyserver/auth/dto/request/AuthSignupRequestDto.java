package cmc.mellyserver.auth.dto.request;

import org.hibernate.validator.constraints.Length;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;
import cmc.mellyserver.dbcore.user.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
public class AuthSignupRequestDto {

  @Email
  private String email; // 이메일 받고

  @Length(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 작성해주세요.")
  private String nickname; // 닉네임 받고

  @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}", message = "비밀번호는 영문과 숫자 조합으로 8자리 이상 가능합니다.")
  private String password; // 비밀번호 받고

  private Gender gender; // 성별 받고

  private AgeGroup ageGroup; // 연령대 받고

  private String fcmToken;

  @Builder
  public AuthSignupRequestDto(String email, String password, String nickname, AgeGroup ageGroup, Gender gender,
	  String fcmToken) {
	this.email = email;
	this.password = password;
	this.nickname = nickname;
	this.ageGroup = ageGroup;
	this.gender = gender;
	this.fcmToken = fcmToken;
  }

  public User toEntity() {
	return User.builder()
		.email(email)
		.password("passowrd")
		.nickname(nickname)
		.ageGroup(ageGroup)
		.gender(gender)
		.fcmToken(fcmToken)
		.roleType(
			RoleType.USER)
		.provider(Provider.DEFAULT)
		.build();
  }

}
