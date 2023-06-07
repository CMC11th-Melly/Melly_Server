package cmc.mellyserver.mellycore.user.domain.repository;

import cmc.mellyserver.mellycore.user.domain.PasswordExpired;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.UserStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId(String userId);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByNickname(String nickname);

    List<User> findByLastModifiedDateBeforeAndUserStatusEquals(LocalDateTime localDateTime,
            UserStatus status);

    List<User> findByPasswordInitDateBeforeAndPasswordExpiredEquals(LocalDateTime localDateTime,
            PasswordExpired passwordExpired);
}
