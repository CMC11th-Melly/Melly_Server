package cmc.mellyserver.dbcore.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findBySocialId(final String socialId);

	Optional<User> findByEmail(final String email);

	Boolean existsByEmail(final String email);

	Boolean existsByNickname(final String nickname);

	Boolean existsByEmailAndPassword(final String email, final String password);
}
