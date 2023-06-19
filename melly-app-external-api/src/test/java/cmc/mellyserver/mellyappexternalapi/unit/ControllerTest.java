package cmc.mellyserver.mellyappexternalapi.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmc.mellyserver.mellyappexternalapi.auth.application.OAuthService;
import cmc.mellyserver.mellyappexternalapi.comment.application.impl.CommentServiceImpl;
import cmc.mellyserver.mellyappexternalapi.comment.presentation.CommentController;
import cmc.mellyserver.mellyappexternalapi.common.annotation.EnableMockMvc;
import cmc.mellyserver.mellyappexternalapi.common.utils.DataExtractor;
import cmc.mellyserver.mellyappexternalapi.config.SecurityConfig;
import cmc.mellyserver.mellyappexternalapi.group.application.GroupService;
import cmc.mellyserver.mellyappexternalapi.group.presentation.GroupController;
import cmc.mellyserver.mellyappexternalapi.memory.application.MemoryService;
import cmc.mellyserver.mellyappexternalapi.memory.presentation.MemoryController;
import cmc.mellyserver.mellyappexternalapi.notification.application.impl.NotificationServiceImpl;
import cmc.mellyserver.mellyappexternalapi.notification.presentation.NotificationController;
import cmc.mellyserver.mellyappexternalapi.place.application.PlaceService;
import cmc.mellyserver.mellyappexternalapi.place.presentation.PlaceController;
import cmc.mellyserver.mellyappexternalapi.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellyappexternalapi.scrap.presentation.PlaceScrapController;
import cmc.mellyserver.mellyappexternalapi.user.application.UserService;
import cmc.mellyserver.mellyappexternalapi.user.presentation.UserController;
import cmc.mellyserver.mellydomain.notification.domain.repository.NotificationRepository;

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
	protected CommentServiceImpl commentService;

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
	protected NotificationServiceImpl notificationService;

	@MockBean
	protected PlaceScrapService placeScrapService;

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected DataExtractor dataExtractor;

	@Autowired
	protected ObjectMapper objectMapper;

}