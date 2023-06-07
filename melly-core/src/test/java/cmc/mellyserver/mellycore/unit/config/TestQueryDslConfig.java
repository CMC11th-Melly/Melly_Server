package cmc.mellyserver.mellycore.unit.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapQueryRepository;

@TestConfiguration
public class TestQueryDslConfig {
	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public PlaceScrapQueryRepository placeScrapQueryRepository() {
		return new PlaceScrapQueryRepository(entityManager);
	}
}