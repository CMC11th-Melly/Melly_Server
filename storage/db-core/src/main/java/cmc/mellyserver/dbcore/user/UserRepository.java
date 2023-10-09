package cmc.mellyserver.dbcore.user;


import cmc.mellyserver.dbcore.user.enums.PasswordExpired;
import cmc.mellyserver.dbcore.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    User findBySocialId(final String socialId);

    Optional<User> findByEmail(final String email);

    Boolean existsByEmail(final String email);

    Boolean existsByNickname(final String nickname);

    Boolean existsByEmailAndPassword(final String email, final String password);

    List<User> findByLastLoginDateTimeBeforeAndUserStatusEquals(final LocalDateTime localDateTime, final UserStatus status);

    List<User> findByPwInitDateTimeBeforeAndPasswordExpiredEquals(final LocalDateTime localDateTime, final PasswordExpired passwordExpired);

}
