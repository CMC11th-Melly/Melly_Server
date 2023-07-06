package cmc.mellyserver.mellyapi.integration;

import cmc.mellyserver.mellyapi.config.DatabaseCleanup;
import cmc.mellyserver.mellycore.comment.application.CommentService;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentLikeRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.application.NotificationService;
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
@ActiveProfiles({"test"}) // 테스트를 실행할 때, test profile을 사용하겠다는 의미이다. profile("test")는 profile이 test일때만 실행하겠다는 의미!
public abstract class IntegrationTest {

    @Autowired
    protected UserService userService;

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected NotificationRepository notificationRepository;

    @Autowired
    protected NotificationService notificationService;

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
