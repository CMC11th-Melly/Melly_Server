package cmc.mellyserver.dbcore.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findBySocialId(String socialId);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByEmailAndPassword(String email, String password);

    List<User> findAllByIdIn(List<Long> userIds);
}
