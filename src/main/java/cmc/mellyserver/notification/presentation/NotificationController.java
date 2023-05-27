package cmc.mellyserver.notification.presentation;

import cmc.mellyserver.common.constants.MessageConstant;
import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.notification.application.NotificationService;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.notification.presentation.dto.NotificationAssembler;
import cmc.mellyserver.notification.presentation.dto.NotificationCheckRequest;
import cmc.mellyserver.user.application.dto.response.NotificationOnOffResponseDto;
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
    public ResponseEntity<CommonResponse> getNotificationOnOff(@AuthenticationPrincipal User user)
    {
        NotificationOnOffResponseDto notificationOnOff = notificationService.getNotificationOnOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(notificationOnOff)));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getNotifications(@AuthenticationPrincipal User user)
    {
        List<Notification> notificationList = notificationService.getNotificationList(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(NotificationAssembler.notificationResponses(notificationList))));
    }

    @PostMapping("/setting/comment/like")
    public ResponseEntity<CommonResponse> appPushOn(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentLikeOn(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @DeleteMapping("/setting/comment/like")
    public ResponseEntity<CommonResponse> appPushOff(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentLikeOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @PostMapping("/setting/comment")
    public ResponseEntity<CommonResponse> appPushCommentOn(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentOn(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @DeleteMapping("/setting/comment")
    public ResponseEntity<CommonResponse> appPushCommentOff(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PostMapping("/setting")
    public ResponseEntity<CommonResponse> appPushCommentLikeOn(@AuthenticationPrincipal User user)
    {
        notificationService.setAppPushOn(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @DeleteMapping("/setting")
    public ResponseEntity<CommonResponse> appPushCommentLikeOff(@AuthenticationPrincipal User user)
    {
        notificationService.setAppPushOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @PostMapping("/check")
    public ResponseEntity<CommonResponse> checkNotification(@RequestBody NotificationCheckRequest notificationCheckRequest)
    {
        notificationService.checkNotification(notificationCheckRequest.getNotificationId());
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }
}
