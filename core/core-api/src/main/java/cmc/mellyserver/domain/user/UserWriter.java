package cmc.mellyserver.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserWriter {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public User save(User user) {
	user.initPassword(passwordEncoder.encode(user.getPassword()));
	return userRepository.save(user);
  }

}
