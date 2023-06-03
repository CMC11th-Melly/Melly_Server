package cmc.mellyserver.mellyapi.acceptance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import cmc.mellyserver.mellyapi.config.DatabaseCleanup;
import cmc.mellyserver.mellyapi.config.InfrastructureTestConfiguration;
import io.restassured.RestAssured;

@Import(InfrastructureTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
public abstract class AcceptanceTest {

	@LocalServerPort
	int port;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	@BeforeEach
	void init() {
		RestAssured.port = port;
	}

	@AfterEach
	void tearDown() {
		clearDataBase();
	}

	private void clearDataBase() {
		databaseCleanup.execute();
	}

}
