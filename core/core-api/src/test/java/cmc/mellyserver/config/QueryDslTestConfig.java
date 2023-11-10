package cmc.mellyserver.config;

import cmc.mellyserver.domain.place.query.PlaceQueryRepository;
import cmc.mellyserver.domain.scrap.query.PlaceScrapQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class QueryDslTestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public PlaceScrapQueryRepository placeScrapQueryRepository() {
        return new PlaceScrapQueryRepository(jpaQueryFactory());
    }

    @Bean
    public PlaceQueryRepository placeQueryRepository() {
        return new PlaceQueryRepository(jpaQueryFactory());
    }




}
