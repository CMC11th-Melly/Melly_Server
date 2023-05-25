package cmc.mellyserver.unit.user.application;

import cmc.mellyserver.common.factory.UserFactory;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.application.impl.UserServiceImpl;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.repository.UserRepository;
import cmc.mellyserver.user.infrastructure.SurveyRecommender;
import com.amazonaws.services.s3.AmazonS3Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private AuthenticatedUserChecker authenticatedUserChecker;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemoryQueryRepository memoryQueryRepository;

    @Mock
    private UserGroupQueryRepository userGroupQueryRepository;

    @Mock
    private GroupService groupService;

    @Mock
    private S3FileLoader s3FileLoader;

    @Mock
    private AmazonS3Client amazonS3Client;

    @Mock
    private SurveyRecommender surveyRecommender;

    @DisplayName("getSurveyResult 메서드는")
    @Nested
    class Describe_getSurveyResult {

        @DisplayName("처음 회원가입을 했을때")
        @Nested
        class When_Signup {

            @DisplayName("사용자의 설문 조사 결과에 따라 장소를 추천받을 수 있다.")
            @Test
            void getSurveyResult_Reccommend_Place() {

                // given
                User emailLoginUser = UserFactory.createEmailLoginUser();
                Long loginUserSeq = emailLoginUser.getUserSeq();

                given(authenticatedUserChecker.checkAuthenticatedUserExist(loginUserSeq))
                        .willReturn(emailLoginUser);

                userService.getSurveyResult(loginUserSeq);


            }
        }


    }}
