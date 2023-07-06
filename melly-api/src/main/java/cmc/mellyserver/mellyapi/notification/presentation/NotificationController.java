package cmc.mellyserver.mellyapi.notification.presentation;

import cmc.mellyserver.mellyapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.notification.presentation.dto.NotificationAssembler;
import cmc.mellyserver.mellyapi.notification.presentation.dto.request.NotificationCheckRequest;
import cmc.mellyserver.mellycore.notification.application.NotificationService;
import cmc.mellyserver.mellycore.notification.application.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.mellycore.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/setting")
    public ResponseEntity<ApiResponse> getNotificationOnOff(@AuthenticationPrincipal User user) {

        NotificationOnOffResponseDto notificationOnOff = notificationService.getNotificationOnOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, notificationOnOff));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getNotifications(@AuthenticationPrincipal User user) {

        List<Notification> notificationList = notificationService.getNotificationList(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, NotificationAssembler.notificationResponses(notificationList)));
    }

    @PostMapping("/setting")
    public ResponseEntity<ApiResponse> appPushOn(@AuthenticationPrincipal User user) {

        notificationService.setAppPushOn(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @DeleteMapping("/setting")
    public ResponseEntity<ApiResponse> appPushOff(@AuthenticationPrincipal User user) {

        notificationService.setAppPushOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PostMapping("/setting/comment")
    public ResponseEntity<ApiResponse> appPushCommentOn(@AuthenticationPrincipal User user) {

        notificationService.setPushCommentOn(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @DeleteMapping("/setting/comment")
    public ResponseEntity<ApiResponse> appPushCommentOff(@AuthenticationPrincipal User user) {

        notificationService.setPushCommentOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PostMapping("/setting/comment/like")
    public ResponseEntity<ApiResponse> appPushCommentLikeOn(@AuthenticationPrincipal User user) {

        notificationService.setPushCommentLikeOn(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @DeleteMapping("/setting/comment/like")
    public ResponseEntity<ApiResponse> appPushCommentLikeOff(@AuthenticationPrincipal User user) {

        notificationService.setPushCommentLikeOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PostMapping("/check")
    public ResponseEntity<ApiResponse> checkNotification(@RequestBody NotificationCheckRequest notificationCheckRequest) {

        notificationService.checkNotification(notificationCheckRequest.getNotificationId());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }
}
