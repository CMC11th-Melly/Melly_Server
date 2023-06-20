package cmc.mellyserver.mellyappexternalapi.integration;

import cmc.mellyserver.mellyappexternalapi.comment.application.impl.CommentServiceImpl;
import cmc.mellyserver.mellyappexternalapi.config.DatabaseCleanup;
import cmc.mellyserver.mellyappexternalapi.group.application.GroupService;
import cmc.mellyserver.mellyappexternalapi.memory.application.MemoryService;
import cmc.mellyserver.mellyappexternalapi.notification.application.impl.NotificationServiceImpl;
import cmc.mellyserver.mellyappexternalapi.place.application.PlaceService;
import cmc.mellyserver.mellyappexternalapi.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellyappexternalapi.user.application.UserService;
import cmc.mellyserver.mellydomain.comment.domain.repository.CommentLikeRepository;
import cmc.mellyserver.mellydomain.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellydomain.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellydomain.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellydomain.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellydomain.notification.domain.repository.NotificationRepository;
import cmc.mellyserver.mellydomain.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellydomain.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellydomain.user.domain.repository.UserRepository;
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
