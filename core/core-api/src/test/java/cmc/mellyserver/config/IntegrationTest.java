package cmc.mellyserver.config;


import cmc.mellyserver.builder.BuilderSupporter;
import cmc.mellyserver.builder.GivenBuilder;
import cmc.mellyserver.common.db.DatabaseCleanup;
import cmc.mellyserver.common.fixture.UserFixtures;
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

    protected GivenBuilder 머식() {
        GivenBuilder 머식 = new GivenBuilder(builderSupporter);
        머식.회원_가입을_한다(UserFixtures.머식_이메일, UserFixtures.머식_닉네임);
        return 머식;
    }
}
