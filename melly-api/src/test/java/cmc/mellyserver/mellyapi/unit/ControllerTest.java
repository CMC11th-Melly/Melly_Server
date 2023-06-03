package cmc.mellyserver.mellyapi.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmc.mellyserver.mellyapi.auth.application.OAuthService;
import cmc.mellyserver.mellyapi.common.annotation.EnableMockMvc;
import cmc.mellyserver.mellyapi.common.utils.DataExtractor;
import cmc.mellyserver.mellyapi.config.SecurityConfig;
import cmc.mellyserver.mellyapi.group.application.GroupService;
import cmc.mellyserver.mellyapi.group.presentation.GroupController;
import cmc.mellyserver.mellyapi.memory.application.MemoryService;
import cmc.mellyserver.mellyapi.memory.presentation.MemoryController;
import cmc.mellyserver.mellyapi.notification.application.NotificationService;
import cmc.mellyserver.mellyapi.notification.presentation.NotificationController;
import cmc.mellyserver.mellyapi.place.application.PlaceService;
import cmc.mellyserver.mellyapi.place.presentation.PlaceController;
import cmc.mellyserver.mellyapi.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellyapi.scrap.presentation.PlaceScrapController;
import cmc.mellyserver.mellyapi.user.application.UserService;
import cmc.mellyserver.mellyapi.user.presentation.UserController;

@AutoConfigureRestDocs
@WebMvcTest(value = {
	UserController.class,
	MemoryController.class,
	GroupController.class,
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