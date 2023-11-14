package cmc.mellyserver.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.user.dto.request.ProfileUpdateRequestDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserWriter {

    private final UserRepository userRepository;

    private final UserReader userReader;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        user.initPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public void update(Long userId, ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user = userReader.findById(userId);
        user.updateProfile(profileUpdateRequestDto.getNickname(), profileUpdateRequestDto.getGender(),
            profileUpdateRequestDto.getAgeGroup());
    }
}
