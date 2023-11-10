package cmc.mellyserver.domain.user.unit;

import static cmc.mellyserver.dbcore.user.enums.AgeGroup.*;
import static cmc.mellyserver.dbcore.user.enums.Gender.*;
import static cmc.mellyserver.fixtures.UserFixtures.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cmc.mellyserver.dbcore.user.Recommend;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.RecommendActivity;
import cmc.mellyserver.dbcore.user.enums.RecommendGroup;
import cmc.mellyserver.dbcore.user.enums.RecommendPlace;

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

	@DisplayName("유저가 최초 회원가입할때 진행한 설문 조사 결과를 저장한다.")
	@Test
	void 회원가입시_진행한_설문조사결과를_저장한다() {

		// given
		User user = 모카();

		// when
		user.addSurveyData(RecommendGroup.FRIEND, RecommendPlace.PLACE1, RecommendActivity.CAFE);

		// then
		Assertions.assertThat(user.getRecommend())
			.usingRecursiveComparison()
			.isEqualTo(new Recommend(RecommendGroup.FRIEND, RecommendPlace.PLACE1, RecommendActivity.CAFE));
	}
}
