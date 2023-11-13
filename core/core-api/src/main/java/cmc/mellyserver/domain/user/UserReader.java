package cmc.mellyserver.domain.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserReader {

	private final UserRepository userRepository;

	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	public boolean existsByNickname(String nickname) {
		return userRepository.existsByNickname(nickname);
	}

	public User findBySocialId(String socialId) {
		return userRepository.findBySocialId(socialId);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean existsByEmailAndPassword(String email, String password) {
		return userRepository.existsByEmailAndPassword(email, password);
	}

}
