package cmc.mellyserver.common.db;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.google.common.base.CaseFormat;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@Profile("test")
public class DatabaseCleanup {

	@PersistenceContext
	private EntityManager entityManager;

	private List<String> tableNames;

	@PostConstruct
	public void init() {
		tableNames = entityManager.getMetamodel()
			.getEntities()
			.stream()
			.filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
			.map(e -> "tb_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
			.collect(Collectors.toList());
		tableNames.add("tb_keyword");
	}

	@Transactional
	public void execute() {
		entityManager.flush();
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

		for (String tableName : tableNames) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
		}

		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}

}
