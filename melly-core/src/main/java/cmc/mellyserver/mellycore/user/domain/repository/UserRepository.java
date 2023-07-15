package cmc.mellyserver.mellycore.user.domain.repository;

import cmc.mellyserver.mellycommon.enums.PasswordExpired;
import cmc.mellyserver.mellycommon.enums.UserStatus;
import cmc.mellyserver.mellycore.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId(String userId);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByNickname(String nickname);

    List<User> findByLastModifiedDateBeforeAndUserStatusEquals(LocalDateTime localDateTime,
                                                               UserStatus status);

    List<User> findByPasswordInitDateBeforeAndPasswordExpiredEquals(LocalDateTime localDateTime,
                                                                    PasswordExpired passwordExpired);
}