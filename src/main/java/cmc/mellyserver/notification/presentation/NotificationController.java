package cmc.mellyserver.notification.presentation;

import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.notification.application.NotificationService;
import cmc.mellyserver.notification.domain.Notification;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 목록 조회(미완성 API)")
    @GetMapping
    public ResponseEntity<CommonResponse> getNotifications(@AuthenticationPrincipal User user)
    {
        List<Notification> notificationList = notificationService.getNotificationList(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"알림 조회",new CommonDetailResponse<>(notificationList)));
    }

}
