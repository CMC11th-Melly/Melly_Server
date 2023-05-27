package cmc.mellyserver.unit;


import cmc.mellyserver.auth.application.AuthService;
import cmc.mellyserver.auth.application.OAuthService;
import cmc.mellyserver.auth.presentation.AuthController;
import cmc.mellyserver.common.annotation.EnableMockMvc;
import cmc.mellyserver.common.utils.DataExtractor;
import cmc.mellyserver.config.SecurityConfig;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.presentation.GroupController;
import cmc.mellyserver.memory.application.MemoryService;
import cmc.mellyserver.memory.presentation.MemoryController;
import cmc.mellyserver.notification.application.NotificationService;
import cmc.mellyserver.notification.presentation.NotificationController;
import cmc.mellyserver.place.application.PlaceService;
import cmc.mellyserver.place.presentation.PlaceController;
import cmc.mellyserver.scrap.application.PlaceScrapService;
import cmc.mellyserver.scrap.presentation.PlaceScrapController;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.presentation.UserController;
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
        PlaceController.class,
        PlaceScrapController.class,
        NotificationController.class
},excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
@EnableMockMvc
@Import(DataExtractor.class)
public class ControllerTest {

    @MockBean
    protected OAuthService oAuthService;


    @MockBean
    protected UserService userService;


    @MockBean
    protected MemoryService memoryService;


    @MockBean
    protected PlaceService placeService;


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
