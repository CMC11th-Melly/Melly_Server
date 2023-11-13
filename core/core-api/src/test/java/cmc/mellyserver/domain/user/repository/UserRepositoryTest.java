package cmc.mellyserver.domain.user.repository;

import static fixtures.UserFixtures.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.support.RepositoryTestSupport;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;

public class UserRepositoryTest extends RepositoryTestSupport {

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
}
