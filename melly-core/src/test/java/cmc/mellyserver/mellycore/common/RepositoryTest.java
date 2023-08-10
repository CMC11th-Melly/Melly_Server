package cmc.mellyserver.mellycore.common;

import cmc.mellyserver.mellycore.config.JpaTestConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaTestConfiguration.class)
public abstract class RepositoryTest {

}
