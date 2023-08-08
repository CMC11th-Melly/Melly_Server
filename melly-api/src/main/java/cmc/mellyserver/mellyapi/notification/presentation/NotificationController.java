package cmc.mellyserver.mellyapi.notification.presentation;

import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.notification.presentation.dto.request.NotificationCheckRequest;
import cmc.mellyserver.mellycore.notification.application.NotificationService;
import cmc.mellyserver.mellycore.notification.application.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.mellycore.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    public ResponseEntity<ApiResponse> getNotificationStatus(@AuthenticationPrincipal User user) {

        NotificationOnOffResponseDto notificationOnOff = notificationService.getNotificationStatus(Long.parseLong(user.getUsername()));
        return OK(notificationOnOff);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getNotifications(@AuthenticationPrincipal User user) {

        List<Notification> notificationList = notificationService.getNotificationList(Long.parseLong(user.getUsername()));
        return OK(notificationResponses(notificationList));
    }

    @PostMapping("/setting")
    public ResponseEntity<Void> changeAppPushStatus(@AuthenticationPrincipal User user, Boolean status) {

        notificationService.changeAppPushStatus(Long.parseLong(user.getUsername()), status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/setting/comment")
    public ResponseEntity<Void> changeCommentPushStatus(@AuthenticationPrincipal User user, Boolean status) {

        notificationService.changeCommentPushStatus(Long.parseLong(user.getUsername()), status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/setting/comment/like")
    public ResponseEntity<Void> changeCommentLikePushStatus(@AuthenticationPrincipal User user, Boolean status) {

        notificationService.changeCommentLikePushStatus(Long.parseLong(user.getUsername()), status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> checkNotification(@RequestBody NotificationCheckRequest notificationCheckRequest) {

        notificationService.checkNotification(notificationCheckRequest.getNotificationId());
        return ResponseEntity.noContent().build();
    }
}
