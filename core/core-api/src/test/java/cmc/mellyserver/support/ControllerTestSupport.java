package cmc.mellyserver.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmc.mellyserver.auth.config.SecurityConfig;
import cmc.mellyserver.auth.token.JwtTokenProvider;
import cmc.mellyserver.controller.comment.CommentController;
import cmc.mellyserver.controller.group.GroupController;
import cmc.mellyserver.controller.memory.MemoryController;
import cmc.mellyserver.controller.notification.NotificationController;
import cmc.mellyserver.controller.place.PlaceController;
import cmc.mellyserver.controller.scrap.PlaceScrapController;
import cmc.mellyserver.controller.user.UserController;
import cmc.mellyserver.domain.comment.CommentLikeService;
import cmc.mellyserver.domain.comment.CommentService;
import cmc.mellyserver.domain.group.GroupService;
import cmc.mellyserver.domain.memory.MemoryService;
import cmc.mellyserver.domain.notification.NotificationService;
import cmc.mellyserver.domain.place.PlaceService;
import cmc.mellyserver.domain.scrap.PlaceScrapService;
import cmc.mellyserver.domain.user.UserProfileService;

@AutoConfigureRestDocs // RestDocs 설정을 활성화 합니다.
@WebMvcTest(controllers = { // 사용하려는 Controller를 모두 등록해줍니다.
	UserController.class, PlaceScrapController.class, PlaceController.class, MemoryController.class,
	GroupController.class, NotificationController.class, CommentController.class},
	excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@ActiveProfiles("test") // test profile로 실행되도록 만듭니다.
public abstract class ControllerTestSupport {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  protected UserProfileService userProfileService;

  @MockBean
  protected PlaceScrapService placeScrapService;

  @MockBean
  protected MemoryService memoryReadService;

  @MockBean
  protected MemoryService memoryService;

  @MockBean
  protected CommentService commentService;

  @MockBean
  protected CommentLikeService commentLikeService;

  @MockBean
  protected PlaceService placeService;

  @MockBean
  protected GroupService groupService;

  @MockBean
  protected NotificationService notificationService;

  @MockBean
  protected JwtTokenProvider jwtTokenProvider;

}
