package cmc.mellyserver.integration;

import cmc.mellyserver.config.DatabaseCleanup;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.group.domain.repository.GroupRepository;
import cmc.mellyserver.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
import cmc.mellyserver.scrap.application.PlaceScrapService;
import cmc.mellyserver.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
