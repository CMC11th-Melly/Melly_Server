package cmc.mellyserver.mellycore.unit.user.domain;

import cmc.mellyserver.mellycommon.enums.*;
import cmc.mellyserver.mellycore.unit.common.fixtures.UserFixtures;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.vo.Recommend;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cmc.mellyserver.mellycommon.enums.AgeGroup.TWO;
import static cmc.mellyserver.mellycommon.enums.Gender.FEMALE;
import static cmc.mellyserver.mellycommon.enums.Gender.MALE;

public class UserTest {

    @DisplayName("유저의 프로필 이미지를 수정한다")
    @Test
    void 유저의_프로필_이미지를_변경한다() {

        // given
        User user = UserFixtures.테스트_유저_1();

        // when
        user.changeProfileImage("updated_image.png");

        // then
        Assertions.assertThat(user.getProfileImage()).isEqualTo("updated_image.png");
    }

    @DisplayName("유저의 프로필 정보를 수정한다")
    @Test
    void 유저의_프로필을_변경한다() {

        // given
        User user = User.builder()
                .nickname("제민")
                .gender(MALE)
                .ageGroup(TWO)
                .build();

        // when
        user.updateProfile("지원", FEMALE, TWO);

        // then
        Assertions.assertThat(user.getNickname()).isEqualTo("지원");
        Assertions.assertThat(user.getGender()).isEqualTo(FEMALE);
        Assertions.assertThat(user.getAgeGroup()).isEqualTo(TWO);
    }

    @DisplayName("유저가 탈퇴한다")
    @Test
    void 유저가_탈퇴한다() {

        // given
        User user = User.builder()
                .nickname("제민")
                .userStatus(UserStatus.ACTIVE).build();

        // when
        user.remove();

        // then
        Assertions.assertThat(user.getUserStatus()).isEqualTo(UserStatus.DELETE);
    }

    @DisplayName("유저가 최초 회원가입할때 진행한 설문 조사 결과를 저장한다")
    @Test
    void 회원가입시_진행한_설문조사결과를_저장한다() {

        // given
        User user = UserFixtures.테스트_유저_1();

        // when
        user.addSurveyData(RecommendGroup.FRIEND, RecommendPlace.PLACE1, RecommendActivity.CAFE);

        // then
        Assertions.assertThat(user.getRecommend()).usingRecursiveComparison()
                .isEqualTo(new Recommend(RecommendGroup.FRIEND,
                        RecommendPlace.PLACE1,
                        RecommendActivity.CAFE));
    }

    @DisplayName("유저의 상태를 휴면상태로 변경한다.")
    @Test
    void 유저의_상태를_휴면상태로_변경() {

        // given
        User user = UserFixtures.테스트_유저_1();

        // when
        user.inActive();

        // then
        Assertions.assertThat(user.getUserStatus()).isEqualTo(UserStatus.INACTIVE);
    }

    @DisplayName("유저 상태를 패스워드 6개월 만료 상태로 변경한다")
    @Test
    void 유저_상태를_패스워드_만료_상태로_변경한다() {

        // given
        User user = UserFixtures.테스트_유저_1();

        // when
        user.setPwChangeStatus();

        // then
        Assertions.assertThat(user.getPasswordExpired()).isEqualTo(PasswordExpired.Y);
    }
}
