package cmc.mellyserver.config;


import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.notification.NotificationRepository;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.comment.query.CommentQueryRepository;
import cmc.mellyserver.domain.group.query.UserGroupQueryRepository;
import cmc.mellyserver.domain.memory.query.MemoryQueryRepository;
import cmc.mellyserver.domain.place.query.PlaceQueryRepository;
import cmc.mellyserver.domain.scrap.query.PlaceScrapQueryRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;


@EnableJpaRepositories(basePackageClasses = {
        UserRepository.class,
        MemoryRepository.class,
        PlaceRepository.class,
        CommentRepository.class,
        PlaceScrapRepository.class,
        JdbcTemplate.class,
        GroupRepository.class,
        GroupAndUserRepository.class,
        NotificationRepository.class}
)
@EnableJpaAuditing
@TestConfiguration
public class JpaTestConfig {

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
