package cmc.mellyserver.unit.user.domain;

import cmc.mellyserver.common.enums.AgeGroup;
import cmc.mellyserver.common.enums.DeleteStatus;
import cmc.mellyserver.common.enums.Gender;
import cmc.mellyserver.common.factory.UserFactory;
import cmc.mellyserver.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class UserTest {

    @DisplayName("유저 프로필 변경 테스트")
    @Test
    void update_user_profile()
    {
        // given
        User user = UserFactory.createEmailLoginUser();

        // when
        user.updateProfile("test_nickname", Gender.FEMALE, null);

        // then
        assertThat(user.getNickname()).isEqualTo("test_nickname");
        assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(user.getAgeGroup()).isEqualTo(AgeGroup.TWO);

    }

    @DisplayName("유저 삭제 테스트")
    @Test
    void remove_user()
    {
        // given
        User user = UserFactory.createEmailLoginUser();

        // when
        user.removeUser();

        // then
        assertThat(user.getIsDeleted()).isEqualTo(DeleteStatus.Y);
    }
}
