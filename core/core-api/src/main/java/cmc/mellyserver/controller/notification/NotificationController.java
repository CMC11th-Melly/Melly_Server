package cmc.mellyserver.controller.notification;

import cmc.mellyserver.controller.notification.dto.request.NotificationCheckRequest;
import cmc.mellyserver.controller.notification.dto.response.NotificationResponse;
import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import cmc.mellyserver.domain.notification.NotificationService;
import cmc.mellyserver.domain.notification.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.common.code.SuccessCode;
import cmc.mellyserver.controller.auth.dto.common.CurrentUser;
import cmc.mellyserver.controller.auth.dto.common.LoginUser;
import cmc.mellyserver.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/setting")
    public ResponseEntity<ApiResponse<NotificationOnOffResponseDto>> getNotificationStatus(@CurrentUser LoginUser loginUser) {

        NotificationOnOffResponseDto notificationOnOff = notificationService.getNotificationStatus(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, notificationOnOff);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveNotification() {
        notificationService.createNotification("내용", NotificationType.COMMENT_ENROLL, 1L, 1L);
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(@CurrentUser LoginUser loginUser) {

        List<NotificationResponse> notificationList = notificationService.getNotificationList(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, notificationList);
    }

    @PostMapping("/setting")
    public ResponseEntity<ApiResponse<Void>> changeAppPushStatus(@CurrentUser LoginUser loginUser, Boolean status) {

        notificationService.changeAppPushStatus(loginUser.getId(), status);
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PostMapping("/setting/comment")
    public ResponseEntity<ApiResponse<Void>> changeCommentPushStatus(@CurrentUser LoginUser loginUser, Boolean status) {

        notificationService.changeCommentPushStatus(loginUser.getId(), status);
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PostMapping("/setting/comment/like")
    public ResponseEntity<ApiResponse<Void>> changeCommentLikePushStatus(@CurrentUser LoginUser loginUser, Boolean status) {

        notificationService.changeCommentLikePushStatus(loginUser.getId(), status);
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PostMapping("/check")
    public ResponseEntity<ApiResponse<Void>> checkNotification(@RequestBody NotificationCheckRequest notificationCheckRequest) {

        notificationService.checkNotification(notificationCheckRequest.getNotificationId());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }
}
