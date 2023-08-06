package cmc.mellyserver.mellycore.unit.user.repository;

import cmc.mellyserver.mellycommon.enums.PasswordExpired;
import cmc.mellyserver.mellycommon.enums.UserStatus;
import cmc.mellyserver.mellycore.unit.RepositoryTest;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;


public class UserRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;


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
