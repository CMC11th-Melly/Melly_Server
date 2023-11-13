package cmc.mellyserver.dbcore.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findBySocialId(String socialId);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

  boolean existsByEmailAndPassword(String email, String password);
}
