package cmc.mellyserver.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

   Optional<User> findUserByUserId(Long userId);

   Optional<User> findUserByEmail(String email);
}
