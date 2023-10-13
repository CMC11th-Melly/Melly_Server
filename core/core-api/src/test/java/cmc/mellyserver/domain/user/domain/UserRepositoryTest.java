package cmc.mellyserver.domain.user.domain;


import cmc.mellyserver.config.RepositoryTest;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.dbcore.user.enums.PasswordExpired;
import cmc.mellyserver.dbcore.user.enums.Provider;
import cmc.mellyserver.dbcore.user.enums.UserStatus;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static cmc.mellyserver.common.constants.UserPolicy.INACTIVE_USER_DURATION;
import static cmc.mellyserver.common.constants.UserPolicy.PASSWORD_CHANGE_DURATION;
import static cmc.mellyserver.common.fixture.UserFixtures.모카;
import static cmc.mellyserver.common.fixture.UserFixtures.모카_이메일;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class UserRepositoryTest extends RepositoryTest {

    private static final LocalDateTime 현재_시간 = LocalDateTime.of(2023, 10, 8, 0, 0);

    @Autowired
    private UserRepository userRepository;

    @DisplayName("중복된 이메일이 존재하는 경우 true를 반환한다.")
    @Test
    void 중복된_이메일이_존재하는_경우_true를_반환한다() {

        // given
        userRepository.save(모카());

        // when & then
        assertThat(userRepository.existsByEmail(모카_이메일)).isTrue();
    }

    @DisplayName("존재하지 않는 유저를 조회하는 경우 예외가 발생한다.")
    @Test
    void 존재하지_않는_유저를_조회하는_경우_예외를_발생한다() {

        // given
        userRepository.save(모카());

        // when & then
        assertThatThrownBy(() -> {
            userRepository.findById(0L).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        }).isInstanceOf(BusinessException.class).hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @DisplayName("유저가 마지막으로 활성화된게 3개월 전이고, 현재 상태가 활성화 상태인 유저 리스트를 조회한다")
    @Test
    void 마지막_로그인_활성화_날짜가_3개월_전인_활성화_유저_조회() {

        // givne
        User 모카 = User.builder().nickname("모카").build();
        모카.updateLastLoginTime(LocalDateTime.of(2023, 2, 1, 0, 0));

        userRepository.save(모카);

        // when
        List<User> result = userRepository.findByLastLoginDateTimeBeforeAndUserStatusEquals(
                현재_시간.minusMonths(INACTIVE_USER_DURATION),
                UserStatus.ACTIVE);

        // then
        Assertions.assertThat(result).hasSize(1);
    }

    @DisplayName("유저가 마지막으로 활성화된 날이 3개월 안쪽이면 조회되지 않는다.")
    @Test
    void 마지막_로그인_활성화_날짜가_3개월_안쪽인_활성화_유저는_조회되지_않는다() {

        // givne
        User 모카 = User.builder().nickname("모카").build();
        모카.updateLastLoginTime(LocalDateTime.of(2023, 8, 1, 0, 0));

        userRepository.save(모카);

        // when
        List<User> result = userRepository.findByLastLoginDateTimeBeforeAndUserStatusEquals(
                현재_시간.minusMonths(INACTIVE_USER_DURATION),
                UserStatus.ACTIVE);

        // then
        Assertions.assertThat(result).hasSize(0);
    }

    @DisplayName("패스워드 초기화 시기가 6개월 전이고, 현재 상태가 활성화인 일반 이메일 유저 리스트를 조회한다")
    @Test
    void 마지막_비밀번호_초기화_날짜가_6개월_전인_활성화_유저_조회() {

        // given
        User 모카 = User.builder().nickname("모카").provider(Provider.DEFAULT).build();

        User savedUser = userRepository.save(모카);
        savedUser.changePwInitDate(LocalDateTime.of(2022, 1, 1, 0, 0));

        // when
        List<User> result = userRepository.findByPwInitDateTimeBeforeAndPasswordExpiredEquals(
                현재_시간.minusMonths(PASSWORD_CHANGE_DURATION), PasswordExpired.N);

        // then
        assertThat(result).hasSize(1);
    }

}
