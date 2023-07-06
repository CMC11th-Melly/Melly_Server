package cmc.mellyserver.mellyapi.unit;

import cmc.mellyserver.mellyapi.auth.application.OAuthService;
import cmc.mellyserver.mellyapi.comment.presentation.CommentController;
import cmc.mellyserver.mellyapi.common.annotation.EnableMockMvc;
import cmc.mellyserver.mellyapi.common.utils.DataExtractor;
import cmc.mellyserver.mellyapi.config.SecurityConfig;
import cmc.mellyserver.mellyapi.group.presentation.GroupController;
import cmc.mellyserver.mellyapi.memory.presentation.MemoryController;
import cmc.mellyserver.mellyapi.notification.presentation.NotificationController;
import cmc.mellyserver.mellyapi.place.presentation.PlaceController;
import cmc.mellyserver.mellyapi.scrap.presentation.PlaceScrapController;
import cmc.mellyserver.mellyapi.user.presentation.UserController;
import cmc.mellyserver.mellycore.comment.application.CommentService;
import cmc.mellyserver.mellycore.notification.application.NotificationService;
import cmc.mellyserver.mellycore.notification.domain.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(value = {
        UserController.class,
        MemoryController.class,
        GroupController.class,
        CommentController.class,
        PlaceController.class,
        PlaceScrapController.class,
        NotificationController.class
}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
@EnableMockMvc
@Import(DataExtractor.class)
public class ControllerTest {

    @MockBean
    protected OAuthService oAuthService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected MemoryService memoryService;

    @MockBean
    protected PlaceService placeService;

    @MockBean
    protected NotificationRepository notificationRepository;

    @MockBean
    protected GroupService groupService;

    @MockBean
    protected NotificationService notificationService;

    @MockBean
    protected PlaceScrapService placeScrapService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected DataExtractor dataExtractor;

    @Autowired
    protected ObjectMapper objectMapper;

}