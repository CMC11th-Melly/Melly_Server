package cmc.mellyserver;

import cmc.mellyserver.dbcore.user.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class UserTest {

    @DisplayName("유저의 프로필 이미지를 수정한다.")
    @Test
    public void 유저의_프로필_이미지를_변경한다() {

        // given
        User user = User.builder().build();

        // when
        user.changeProfileImage("updated_image.png");

        // then
        Assertions.assertThat(user.getProfileImage()).isEqualTo("updated_image.png");
    }
}
