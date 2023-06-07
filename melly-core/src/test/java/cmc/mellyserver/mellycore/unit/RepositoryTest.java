package cmc.mellyserver.mellycore.unit;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import cmc.mellyserver.mellycore.unit.config.TestQueryDslConfig;

@DataJpaTest
@Import(TestQueryDslConfig.class)
public abstract class RepositoryTest {
}
