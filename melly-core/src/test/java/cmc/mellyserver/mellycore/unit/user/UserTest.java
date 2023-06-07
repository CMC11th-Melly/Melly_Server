package cmc.mellyserver.mellycore.unit.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.common.enums.DeleteStatus;
import cmc.mellyserver.mellycore.common.enums.Gender;
import cmc.mellyserver.mellycore.common.enums.Provider;
import cmc.mellyserver.mellycore.common.enums.RecommendActivity;
import cmc.mellyserver.mellycore.common.enums.RecommendGroup;
import cmc.mellyserver.mellycore.common.enums.RecommendPlace;
import cmc.mellyserver.mellycore.user.domain.Recommend;
import cmc.mellyserver.mellycore.user.domain.User;

public class UserTest {

	@DisplayName("유저의 프로필 이미지를 변경할 수 있다.")
	@Test
	void change_user_profile_iamge() {

		// given
		User user = User.builder().nickname("테스트 유저").profileImage("test_image.png").build();

		// when
		user.chnageProfileImage("updated_image.png");

		// then
		Assertions.assertThat(user.getProfileImage()).isEqualTo("updated_image.png");
	}

	@DisplayName("유저를 프로필 정보를 업데이트 할 수 있다.")
	@Test
	void update_user_profile() {

		// given
		User user = User.builder()
			.nickname("원래 닉네임")
			.gender(Gender.MALE)
			.provider(Provider.GOOGLE)
			.ageGroup(AgeGroup.TWO)
			.build();

		// when
		user.updateProfile("원래 닉네임", Gender.FEMALE, AgeGroup.THREE);

		// then
		Assertions.assertThat(user.getNickname()).isEqualTo("원래 닉네임");
		Assertions.assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
		Assertions.assertThat(user.getAgeGroup()).isEqualTo(AgeGroup.THREE);
	}

	@DisplayName("유저를 soft-delete 처리할 수 있다.")
	@Test
	void remove_user() {

		// given
		User user = User.builder().nickname("테스트 유저").isDeleted(DeleteStatus.N).build();

		// when
		user.removeUser();

		// then
		Assertions.assertThat(user.getIsDeleted()).isEqualTo(DeleteStatus.Y);
	}

	@DisplayName("유저가 최초 회원가입할때 진행한 설문 조사 결과를 저장한다.")
	@Test
	void add_survey_data() {

		// given
		User user = User.builder().nickname("테스트 유저").isDeleted(DeleteStatus.N).build();

		// when
		user.addSurveyData(RecommendGroup.FRIEND, RecommendPlace.PLACE1, RecommendActivity.CAFE);

		// then
		Assertions.assertThat(user.getRecommend()).usingRecursiveComparison()
			.isEqualTo(new Recommend(RecommendGroup.FRIEND, RecommendPlace.PLACE1, RecommendActivity.CAFE));
	}
}
