package cmc.mellyserver.domain.user;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWriter {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }
}
