package cmc.mellyserver.mellycore.unit.user.repository;

import cmc.mellyserver.mellycore.common.enums.Gender;
import cmc.mellyserver.mellycore.unit.RepositoryTest;
import cmc.mellyserver.mellycore.unit.common.fixtures.UserFixtures;
import cmc.mellyserver.mellycore.user.domain.PasswordExpired;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.UserStatus;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


public class UserRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private List<User> users;

    @BeforeEach
    void setUp() {
        // given
        users = UserFixtures.mockLikeUsersWithId();
        users.forEach(user -> userRepository.save(user));
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @DisplayName("유저 ID로 유저를 조회할 수 있다.")
    @Test
    void find_user_by_user_id() {

        // when
        User user = userRepository.findUserByUserId(users.get(0).getUserId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        Assertions.assertThat(user.getGender())
                .isEqualTo(Gender.FEMALE);
    }

    @DisplayName("유저 이메일로 유저를 조회할 수 있다.")
    @Test
    void find_user_by_email() {

        // when
        User user = userRepository.findUserByEmail(users.get(0).getEmail())
                .orElseThrow(IllegalArgumentException::new);

        // then
        Assertions.assertThat(user.getGender())
                .isEqualTo(Gender.FEMALE);
    }

    @DisplayName("유저 닉네임으로 유저를 조회할 수 있다.")
    @Test
    void find_user_by_nickname() {

        // when
        User user = userRepository.findUserByNickname(users.get(0).getNickname())
                .orElseThrow(IllegalArgumentException::new);

        // then
        Assertions.assertThat(user.getGender())
                .isEqualTo(Gender.FEMALE);
    }

    @DisplayName("유저가 마지막으로 활성화된게 6개월 전이고, 현재 상태가 활성화 상태인 것들 조회")
    @Test
    void find_not_active_user() {

        // when
        List<User> result = userRepository.findByLastModifiedDateBeforeAndUserStatusEquals(
                LocalDateTime.now().plusMonths(6), UserStatus.ACTIVE);

        // then
        Assertions.assertThat(result).hasSize(5);
    }

    @DisplayName("패스워드 변경 시기가 되고, 현재 상태가 활성화인 일반 이메일 유저들 조회")
    @Test
    void find_password_expired_user() {

        // when
        List<User> result = userRepository.findByPasswordInitDateBeforeAndPasswordExpiredEquals(
                LocalDateTime.now().plusMonths(6), PasswordExpired.N);

        // then
        Assertions.assertThat(result).hasSize(2);
    }

}
