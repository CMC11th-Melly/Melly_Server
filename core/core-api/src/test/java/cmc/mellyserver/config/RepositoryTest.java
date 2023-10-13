package cmc.mellyserver.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(JpaTestConfig.class)
public abstract class RepositoryTest {

}
