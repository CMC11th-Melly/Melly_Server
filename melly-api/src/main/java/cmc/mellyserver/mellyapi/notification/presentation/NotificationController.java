package cmc.mellyserver.mellyapi.notification.presentation;

import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.notification.presentation.dto.request.NotificationCheckRequest;
import cmc.mellyserver.mellycore.notification.application.NotificationService;
import cmc.mellyserver.mellycore.notification.application.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.mellycore.notification.domain.Notification;
import cmc.mellyserver.mellycore.notification.domain.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cmc.mellyserver.mellyapi.common.response.ApiResponse.OK;
import static cmc.mellyserver.mellyapi.notification.presentation.dto.NotificationAssembler.notificationResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/setting")
    public ResponseEntity<ApiResponse> getNotificationStatus(@CurrentUser LoginUser loginUser) {

        NotificationOnOffResponseDto notificationOnOff = notificationService.getNotificationStatus(loginUser.getId());
        return OK(notificationOnOff);
    }

    @PostMapping
    public ResponseEntity<Void> saveNotification() {
        notificationService.createNotification("제목", "내용", NotificationType.COMMENT, 1L, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getNotifications(@CurrentUser LoginUser loginUser) {

        List<Notification> notificationList = notificationService.getNotificationList(loginUser.getId());
        return OK(notificationResponses(notificationList));
    }

    @PostMapping("/setting")
    public ResponseEntity<Void> changeAppPushStatus(@CurrentUser LoginUser loginUser, Boolean status) {

        notificationService.changeAppPushStatus(loginUser.getId(), status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/setting/comment")
    public ResponseEntity<Void> changeCommentPushStatus(@CurrentUser LoginUser loginUser, Boolean status) {

        notificationService.changeCommentPushStatus(loginUser.getId(), status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/setting/comment/like")
    public ResponseEntity<Void> changeCommentLikePushStatus(@CurrentUser LoginUser loginUser, Boolean status) {

        notificationService.changeCommentLikePushStatus(loginUser.getId(), status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> checkNotification(@RequestBody NotificationCheckRequest notificationCheckRequest) {

        notificationService.checkNotification(notificationCheckRequest.getNotificationId());
        return ResponseEntity.noContent().build();
    }
}
