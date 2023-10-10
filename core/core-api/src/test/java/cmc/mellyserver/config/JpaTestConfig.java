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
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;


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
    public PlaceScrapQueryRepository placeScrapQueryRepository(JPAQueryFactory jpaQueryFactory) {
        return new PlaceScrapQueryRepository(jpaQueryFactory);
    }

    @Bean
    public PlaceQueryRepository placeQueryRepository(JPAQueryFactory jpaQueryFactory) {
        return new PlaceQueryRepository(jpaQueryFactory);
    }

    @Bean
    public MemoryQueryRepository memoryQueryRepository(JPAQueryFactory jpaQueryFactory) {
        return new MemoryQueryRepository(jpaQueryFactory);
    }

    @Bean
    public CommentQueryRepository commentQueryRepository(JPAQueryFactory jpaQueryFactory) {
        return new CommentQueryRepository(jpaQueryFactory);
    }

    @Bean
    public UserGroupQueryRepository userGroupQueryRepository(JPAQueryFactory jpaQueryFactory) {
        return new UserGroupQueryRepository(jpaQueryFactory);
    }
}
