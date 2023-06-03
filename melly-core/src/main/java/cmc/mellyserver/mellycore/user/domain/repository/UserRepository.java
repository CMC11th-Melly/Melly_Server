package cmc.mellyserver.mellycore.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cmc.mellyserver.mellycore.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findUserByUserId(String userId);

	Optional<User> findUserByEmail(String email);

	Optional<User> findUserByNickname(String nickname);
}
