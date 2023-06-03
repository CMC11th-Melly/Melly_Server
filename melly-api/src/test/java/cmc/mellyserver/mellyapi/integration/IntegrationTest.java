package cmc.mellyserver.mellyapi.integration;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.mellyapi.config.DatabaseCleanup;
import cmc.mellyserver.mellyapi.group.application.GroupService;
import cmc.mellyserver.mellyapi.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellyapi.user.application.UserService;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;

@Transactional
@SpringBootTest
@ActiveProfiles({"test"})
public abstract class IntegrationTest {

	@Autowired
	protected UserService userService;

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected GroupRepository groupRepository;

	@Autowired
	protected GroupAndUserRepository groupAndUserRepository;

	@Autowired
	protected MemoryRepository memoryRepository;

	@Autowired
	protected GroupService groupService;

	@Autowired
	protected PlaceRepository placeRepository;

	@Autowired
	protected PlaceScrapService placeScrapService;

	@Autowired
	protected PlaceScrapRepository placeScrapRepository;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	@AfterEach
	public void clean() {
		databaseCleanup.execute();
	}
}
