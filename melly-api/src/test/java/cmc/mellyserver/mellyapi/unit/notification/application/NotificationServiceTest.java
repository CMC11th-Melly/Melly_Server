package cmc.mellyserver.mellyapi.unit.notification.application;

import cmc.mellyserver.mellyapi.common.factory.UserFactory;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.application.NotificationService;
import cmc.mellyserver.mellycore.notification.domain.repository.NotificationRepository;
import cmc.mellyserver.mellycore.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    @Mock
    AuthenticatedUserChecker authenticatedUserChecker;

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    MemoryRepository memoryRepository;

    @DisplayName("앱 푸시 동의 on/off를 설정할 수 있다.")
    @Test
    void set_app_push_onOff() {

        // given
        User emailLoginUser = UserFactory.createEmailLoginUser();
        given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong()))
                .willReturn(emailLoginUser);

        // when then
        notificationService.setAppPushOn(1L);
        Assertions.assertThat(emailLoginUser.isEnableAppPush()).isTrue();
        notificationService.setAppPushOff(2L);
        Assertions.assertThat(emailLoginUser.isEnableAppPush()).isFalse();
    }

    @DisplayName("댓글 작성시 알람 수신 여부를 설정할 수 있다.")
    @Test
    void set_comment_push_onOff() {

        // given
        User emailLoginUser = UserFactory.createEmailLoginUser();
        given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong()))
                .willReturn(emailLoginUser);

        // when then
        notificationService.setPushCommentOn(1L);
        Assertions.assertThat(emailLoginUser.isEnableComment()).isTrue();
        notificationService.setPushCommentOff(2L);
        Assertions.assertThat(emailLoginUser.isEnableComment()).isFalse();
    }

    @DisplayName("댓글 좋아요 시 알람 수신 여부를 설정할 수 있다.")
    @Test
    void set_comment_like_push_onOff() {

        // given
        User emailLoginUser = UserFactory.createEmailLoginUser();
        given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong()))
                .willReturn(emailLoginUser);

        // when then
        notificationService.setPushCommentLikeOn(1L);
        Assertions.assertThat(emailLoginUser.isEnableCommentLike()).isTrue();
        notificationService.setPushCommentLikeOff(2L);
        Assertions.assertThat(emailLoginUser.isEnableCommentLike()).isFalse();
    }

}
