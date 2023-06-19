package cmc.mellyserver.mellydomain.unit.user.domain;

import cmc.mellyserver.mellydomain.common.enums.*;
import cmc.mellyserver.mellydomain.user.domain.User;
import cmc.mellyserver.mellydomain.user.domain.vo.Recommend;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class UserTest {

    @DisplayName("유저의 프로필 이미지를 변경할 수 있다.")
    @Test
    void 유저의_프로필_이미지를_변경한다() {

        // given
        User user = User.builder().nickname("테스트 유저").profileImage("test_image.png").build();

        // when
        user.chnageProfileImage("updated_image.png");

        // then
        Assertions.assertThat(user.getProfileImage()).isEqualTo("updated_image.png");
    }

    @DisplayName("유저를 프로필 정보를 업데이트 할 수 있다.")
    @Test
    void 유저의_프로필을_변경한다() {

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
    void 유저를_삭제한다() {

        // given
        User user = User.builder().nickname("테스트 유저").isDeleted(DeleteStatus.N).build();

        // when
        user.removeUser();

        // then
        Assertions.assertThat(user.getIsDeleted()).isEqualTo(DeleteStatus.Y);
    }

    @DisplayName("유저가 최초 회원가입할때 진행한 설문 조사 결과를 저장한다.")
    @Test
    void 회원가입시_진행한_설문조사결과를_저장한다() {

        // given
        User user = User.builder().nickname("테스트 유저").isDeleted(DeleteStatus.N).build();

        // when
        user.addSurveyData(RecommendGroup.FRIEND, RecommendPlace.PLACE1, RecommendActivity.CAFE);

        // then
        Assertions.assertThat(user.getRecommend()).usingRecursiveComparison()
                .isEqualTo(new Recommend(RecommendGroup.FRIEND, RecommendPlace.PLACE1,
                        RecommendActivity.CAFE));
    }

    @DisplayName("유저의 상태를 휴면상태로 변경한다.")
    @Test
    void 유저의_상태를_휴면상태로_변경() {

        // given
        User user = User.builder().nickname("테스트 유저").isDeleted(DeleteStatus.N)
                .userStatus(UserStatus.ACTIVE).build();

        // when
        user.setInactive();

        // then
        Assertions.assertThat(user.getUserStatus()).isEqualTo(UserStatus.INACTIVE);
    }

    @DisplayName("패스워드 만료 여부를 설정하고 패스워드 초기화 기간 설정")
    @Test
    void 패스워드_만료상태를_설정하고_패스워드_초기화_기간을_설정() {

        // given
        User user = User.builder().nickname("테스트 유저").isDeleted(DeleteStatus.N)
                .userStatus(UserStatus.ACTIVE).passwordExpired(
                        PasswordExpired.N).passwordInitDate(LocalDateTime.of(2023, 6, 1, 10, 50))
                .build();

        // when
        user.setPwChangeStatusAndUpdateLastChangedDate(LocalDateTime.of(2023, 6, 2, 10, 50));

        // then
        Assertions.assertThat(user.getPasswordExpired()).isEqualTo(PasswordExpired.Y);
        Assertions.assertThat(user.getPasswordInitDate())
                .isEqualTo(LocalDateTime.of(2023, 6, 2, 10, 50));
    }

}
