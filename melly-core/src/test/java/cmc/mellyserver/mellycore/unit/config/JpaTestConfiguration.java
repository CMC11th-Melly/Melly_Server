package cmc.mellyserver.mellycore.unit.config;

import cmc.mellyserver.mellycore.comment.domain.repository.CommentQueryRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.domain.repository.NotificationRepository;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapQueryRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, MemoryRepository.class,
        PlaceRepository.class, CommentRepository.class, PlaceScrapRepository.class,
        GroupRepository.class, GroupAndUserRepository.class,
        NotificationRepository.class})
@TestConfiguration
public class JpaTestConfiguration {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public PlaceScrapQueryRepository placeScrapQueryRepository(EntityManager entityManager) {
        return new PlaceScrapQueryRepository(entityManager);
    }

    @Bean
    public PlaceQueryRepository placeQueryRepository(EntityManager entityManager) {
        return new PlaceQueryRepository(entityManager);
    }

    @Bean
    public MemoryQueryRepository memoryQueryRepository(EntityManager entityManager) {
        return new MemoryQueryRepository(entityManager);
    }

    @Bean
    public CommentQueryRepository commentQueryRepository(EntityManager entityManager) {
        return new CommentQueryRepository(entityManager);
    }

    @Bean
    public UserGroupQueryRepository userGroupQueryRepository(EntityManager entityManager) {
        return new UserGroupQueryRepository(entityManager);
    }
}
