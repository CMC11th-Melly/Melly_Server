package cmc.mellyserver.notification.presentation;

import cmc.mellyserver.common.constants.MessageConstant;
import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.notification.application.NotificationService;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.notification.presentation.dto.NotificationAssembler;
import cmc.mellyserver.notification.presentation.dto.NotificationCheckRequest;
import cmc.mellyserver.user.presentation.dto.response.NotificationOnOffResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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


    @Operation(summary = "유저 푸시 권한 목록")
    @GetMapping("/setting")
    public ResponseEntity<CommonResponse> getNotificationOnOff(@AuthenticationPrincipal User user)
    {
        NotificationOnOffResponseDto notificationOnOff = notificationService.getNotificationOnOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(notificationOnOff)));
    }

    @Operation(summary = "알림 목록 조회(테스트 필요)")
    @GetMapping
    public ResponseEntity<CommonResponse> getNotifications(@AuthenticationPrincipal User user)
    {
        List<Notification> notificationList = notificationService.getNotificationList(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(NotificationAssembler.notificationResponses(notificationList))));
    }

    @Operation(summary = "댓글 좋아요 ON")
    @PostMapping("/setting/comment/like")
    public ResponseEntity<CommonResponse> appPushOn(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentLikeOn(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @Operation(summary = "댓글 좋아요 OFF")
    @DeleteMapping("/setting/comment/like")
    public ResponseEntity<CommonResponse> appPushOff(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentLikeOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @Operation(summary = "댓글 수신 ON")
    @PostMapping("/setting/comment")
    public ResponseEntity<CommonResponse> appPushCommentOn(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentOn(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @Operation(summary = "댓글 수신 OFF")
    @DeleteMapping("/setting/comment")
    public ResponseEntity<CommonResponse> appPushCommentOff(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @Operation(summary = "유저 앱 푸시 ON")
    @PostMapping("/setting")
    public ResponseEntity<CommonResponse> appPushCommentLikeOn(@AuthenticationPrincipal User user)
    {
        notificationService.setAppPushOn(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @Operation(summary = "유저 앱 푸시 OFF")
    @DeleteMapping("/setting")
    public ResponseEntity<CommonResponse> appPushCommentLikeOff(@AuthenticationPrincipal User user)
    {
        notificationService.setAppPushOff(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @Operation(summary = "알림 읽음 처리")
    @PostMapping("/check")
    public ResponseEntity<CommonResponse> checkNotification(@RequestBody NotificationCheckRequest notificationCheckRequest)
    {
        notificationService.checkNotification(notificationCheckRequest.getNotificationId());
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


}
