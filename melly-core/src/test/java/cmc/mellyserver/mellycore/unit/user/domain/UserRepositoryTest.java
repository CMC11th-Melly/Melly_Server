package cmc.mellyserver.mellycore.unit.user.domain;

import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycommon.enums.PasswordExpired;
import cmc.mellyserver.mellycore.common.enums.UserStatus;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.unit.RepositoryTest;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static cmc.mellyserver.mellycore.common.fixture.UserFixtures.모카;
import static cmc.mellyserver.mellycore.common.fixture.UserFixtures.모카_이메일;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class UserRepositoryTest extends RepositoryTest {

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
            userRepository.getById(0L);
        }).isInstanceOf(BusinessException.class).hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @DisplayName("유저가 마지막으로 활성화된게 6개월 전이고, 현재 상태가 활성화 상태인 유저 리스트를 조회한다")
    @Test
    void 마지막_로그인_활성화_날짜가_6개월_전인_활성화_유저_조회() {

        // when
        List<User> result = userRepository.findByLastLoginDateTimeBeforeAndUserStatusEquals(
                LocalDateTime.now().plusMonths(6), UserStatus.ACTIVE);

    }

    @DisplayName("패스워드 초기화 시기가 6개월 전이고, 현재 상태가 활성화인 일반 이메일 유저 리스트를 조회한다")
    @Test
    void 마지막_비밀번호_초기화_날짜가_6개월_전인_활성화_유저_조회() {

        // when
        List<User> result = userRepository.findByPwInitDateTimeBeforeAndPasswordExpiredEquals(
                LocalDateTime.now().plusMonths(6), PasswordExpired.N);

    }

}
