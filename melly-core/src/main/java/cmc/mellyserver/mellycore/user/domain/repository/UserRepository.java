package cmc.mellyserver.mellycore.user.domain.repository;

import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.PasswordExpired;
import cmc.mellyserver.mellycore.user.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static cmc.mellyserver.mellycore.common.exception.ErrorCode.USER_NOT_FOUND;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserBySocialId(final String socialId);

    Optional<User> findUserByEmail(final String email);

    Boolean existsByEmail(final String email);

    Boolean existsByNickname(final String nickname);

    default User getById(final Long userId) {
        return findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    Boolean existsByEmailAndPassword(final String email, final String password);

    List<User> findByLastLoginDateTimeBeforeAndUserStatusEquals(final LocalDateTime localDateTime, final UserStatus status);

    List<User> findByPwInitDateTimeBeforeAndPasswordExpiredEquals(final LocalDateTime localDateTime, final PasswordExpired passwordExpired);
}
