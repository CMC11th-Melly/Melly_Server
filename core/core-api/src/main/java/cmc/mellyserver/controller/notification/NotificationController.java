package cmc.mellyserver.controller.notification;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.auth.common.resolver.CurrentUser;
import cmc.mellyserver.auth.common.resolver.LoginUser;
import cmc.mellyserver.controller.notification.dto.request.NotificationCheckRequest;
import cmc.mellyserver.controller.notification.dto.response.NotificationResponse;
import cmc.mellyserver.domain.notification.NotificationService;
import cmc.mellyserver.domain.notification.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.support.response.ApiResponse;
import cmc.mellyserver.support.response.SuccessCode;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(@CurrentUser LoginUser loginUser) {

        List<NotificationResponse> notificationList = notificationService.getNotificationList(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, notificationList);
    }

    @GetMapping("/setting")
    public ResponseEntity<ApiResponse<NotificationOnOffResponseDto>> getNotificationStatus(
        @CurrentUser LoginUser loginUser) {

        NotificationOnOffResponseDto notificationOnOff = notificationService.getNotificationStatus(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, notificationOnOff);
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

    @PostMapping("/check")
    public ResponseEntity<ApiResponse<Void>> checkNotification(
        @RequestBody NotificationCheckRequest notificationCheckRequest) {

        notificationService.readNotification(notificationCheckRequest.getNotificationId());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

}
