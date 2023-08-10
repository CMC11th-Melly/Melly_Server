package cmc.mellyserver.mellycore.common;


import cmc.mellyserver.mellycore.builder.BuilderSupporter;
import cmc.mellyserver.mellycore.builder.GivenBuilder;
import cmc.mellyserver.mellycore.common.db.DatabaseCleanup;
import cmc.mellyserver.mellycore.common.fixture.UserFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class IntegrationTest {


    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private BuilderSupporter builderSupporter;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
    }

    protected GivenBuilder 모카() {
        GivenBuilder 모카 = new GivenBuilder(builderSupporter);
        모카.회원_가입을_한다(UserFixtures.모카_이메일, UserFixtures.모카_닉네임);
        return 모카;
    }
}
