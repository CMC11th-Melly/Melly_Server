package cmc.mellyserver.mellyapi.integration;

import cmc.mellyserver.mellyapi.comment.application.impl.CommentServiceImpl;
import cmc.mellyserver.mellyapi.config.DatabaseCleanup;
import cmc.mellyserver.mellyapi.group.application.GroupService;
import cmc.mellyserver.mellyapi.memory.application.MemoryService;
import cmc.mellyserver.mellyapi.notification.application.impl.NotificationServiceImpl;
import cmc.mellyserver.mellyapi.place.application.PlaceService;
import cmc.mellyserver.mellyapi.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellyapi.user.application.UserService;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentLikeRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.domain.repository.NotificationRepository;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
    protected CommentServiceImpl commentService;

    @Autowired
    protected NotificationRepository notificationRepository;

    @Autowired
    protected NotificationServiceImpl notificationService;

    @Autowired
    protected CommentLikeRepository commentLikeRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected GroupRepository groupRepository;

    @Autowired
    protected MemoryService memoryService;

    @Autowired
    protected GroupAndUserRepository groupAndUserRepository;

    @Autowired
    protected MemoryRepository memoryRepository;

    @Autowired
    protected GroupService groupService;

    @Autowired
    protected PlaceService placeService;

    @Autowired
    protected PlaceRepository placeRepository;

    @Autowired
    protected PlaceScrapService placeScrapService;

    @Autowired
    protected PlaceScrapRepository placeScrapRepository;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void clean() {
        databaseCleanup.execute();
    }
}
