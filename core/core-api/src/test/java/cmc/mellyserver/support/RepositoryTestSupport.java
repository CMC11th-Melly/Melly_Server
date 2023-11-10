package cmc.mellyserver.support;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import cmc.mellyserver.config.QueryDslTestConfig;

@DataJpaTest
@Import(QueryDslTestConfig.class)
@ActiveProfiles("test")
public abstract class RepositoryTestSupport {

}
