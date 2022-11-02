package cmc.mellyserver.notification.presentation;

import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.notification.application.NotificationService;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.notification.presentation.dto.NotificationAssembler;
import cmc.mellyserver.user.presentation.dto.NotificationOnOffResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
        NotificationOnOffResponse notificationOnOff = notificationService.getNotificationOnOff(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"성공",new CommonDetailResponse<>(notificationOnOff)));
    }

    @Operation(summary = "알림 목록 조회(미완성 API)")
    @GetMapping
    public ResponseEntity<CommonResponse> getNotifications(@AuthenticationPrincipal User user)
    {
        List<Notification> notificationList = notificationService.getNotificationList(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"알림 조회",new CommonDetailResponse<>(NotificationAssembler.notificationResponses(notificationList))));
    }

    @Operation(summary = "댓글 좋아요 ON")
    @PostMapping("/setting/comment/like")
    public ResponseEntity<CommonResponse> appPushOn(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentLikeOn(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }

    @Operation(summary = "댓글 좋아요 OFF")
    @DeleteMapping("/setting/comment/like")
    public ResponseEntity<CommonResponse> appPushOff(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentLikeOff(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }


    @Operation(summary = "댓글 수신 ON")
    @PostMapping("/setting/comment")
    public ResponseEntity<CommonResponse> appPushCommentOn(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentOn(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }

    @Operation(summary = "댓글 수신 OFF")
    @DeleteMapping("/setting/comment")
    public ResponseEntity<CommonResponse> appPushCommentOff(@AuthenticationPrincipal User user)
    {
        notificationService.setPushCommentOff(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }

    @Operation(summary = "유저 앱 푸시 ON")
    @PostMapping("/setting")
    public ResponseEntity<CommonResponse> appPushCommentLikeOn(@AuthenticationPrincipal User user)
    {
        notificationService.setAppPushOn(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }

    @Operation(summary = "유저 앱 푸시 OFF")
    @DeleteMapping("/setting")
    public ResponseEntity<CommonResponse> appPushCommentLikeOff(@AuthenticationPrincipal User user)
    {
        notificationService.setAppPushOff(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }



}
