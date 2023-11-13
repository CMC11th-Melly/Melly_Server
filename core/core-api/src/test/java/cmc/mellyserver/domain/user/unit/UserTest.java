package cmc.mellyserver.domain.user.unit;

import static cmc.mellyserver.dbcore.user.enums.AgeGroup.*;
import static cmc.mellyserver.dbcore.user.enums.Gender.*;
import static fixtures.UserFixtures.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cmc.mellyserver.dbcore.user.User;

public class UserTest {

	@DisplayName("유저의 프로필 이미지를 수정한다.")
	@Test
	void 유저의_프로필_이미지를_변경한다() {

		// given
		User user = 모카();

		// when
		user.changeProfileImage("updated_image.png");

		// then
		Assertions.assertThat(user.getProfileImage()).isEqualTo("updated_image.png");
	}

	@DisplayName("유저의 프로필 정보를 수정한다.")
	@Test
	void 유저의_프로필을_변경한다() {

		// given
		User user = 모카();

		// when
		user.updateProfile("지원", FEMALE, TWO);

		// then
		Assertions.assertThat(user.getNickname()).isEqualTo("지원");
		Assertions.assertThat(user.getGender()).isEqualTo(FEMALE);
		Assertions.assertThat(user.getAgeGroup()).isEqualTo(TWO);
	}
}
