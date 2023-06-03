package cmc.mellyserver.mellyapi.common.auth;

import org.springframework.stereotype.Component;

import cmc.mellyserver.mellyapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserChecker {
	private final UserRepository userRepository;

	public User checkAuthenticatedUserExist(Long userSeq) {
		return userRepository.findById(userSeq).orElseThrow(() -> {
			throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
		});
	}
}
