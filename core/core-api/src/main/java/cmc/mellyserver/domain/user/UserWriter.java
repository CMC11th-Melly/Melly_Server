package cmc.mellyserver.domain.user;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserWriter {

	private final UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}

}
