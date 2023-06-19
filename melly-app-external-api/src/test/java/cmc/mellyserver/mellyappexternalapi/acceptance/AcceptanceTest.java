package cmc.mellyserver.mellyappexternalapi.acceptance;

import cmc.mellyserver.mellyappexternalapi.config.DatabaseCleanup;
import cmc.mellyserver.mellyappexternalapi.config.InfrastructureTestConfiguration;
import cmc.mellyserver.mellydomain.common.enums.RoleType;
import cmc.mellyserver.mellydomain.user.domain.User;
import cmc.mellyserver.mellydomain.user.domain.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@Import(InfrastructureTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
public abstract class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        userRepository.save(
                User.builder().email("jemin3161@naver.com").nickname("제민")
                        .password(passwordEncoder.encode("1234mm1234"))
                        .roleType(
                                RoleType.USER)
                        .build());
    }

    @AfterEach
    void tearDown() {
        clearDataBase();
    }

    private void clearDataBase() {
        databaseCleanup.execute();
    }

}
