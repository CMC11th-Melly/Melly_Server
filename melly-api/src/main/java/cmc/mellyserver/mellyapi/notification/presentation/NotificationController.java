package cmc.mellyserver.mellyapi.notification.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.mellyapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyapi.common.response.CommonResponse;
import cmc.mellyserver.mellyapi.notification.application.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.mellyapi.notification.application.impl.NotificationServiceImpl;
import cmc.mellyserver.mellyapi.notification.presentation.dto.NotificationAssembler;
import cmc.mellyserver.mellyapi.notification.presentation.dto.request.NotificationCheckRequest;
import cmc.mellyserver.mellycore.notification.domain.Notification;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

	private final NotificationServiceImpl notificationService;

	@GetMapping("/setting")
	public ResponseEntity<CommonResponse> getNotificationOnOff(@AuthenticationPrincipal User user) {
		NotificationOnOffResponseDto notificationOnOff = notificationService.getNotificationOnOff(
			Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(
			new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, notificationOnOff));
	}

	@GetMapping
	public ResponseEntity<CommonResponse> getNotifications(@AuthenticationPrincipal User user) {
		List<Notification> notificationList = notificationService.getNotificationList(
			Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
			NotificationAssembler.notificationResponses(notificationList)));
	}

	@PostMapping("/setting")
	public ResponseEntity<CommonResponse> appPushOn(@AuthenticationPrincipal User user) {
		notificationService.setAppPushOn(Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@DeleteMapping("/setting")
	public ResponseEntity<CommonResponse> appPushOff(@AuthenticationPrincipal User user) {
		notificationService.setAppPushOff(Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@PostMapping("/setting/comment")
	public ResponseEntity<CommonResponse> appPushCommentOn(@AuthenticationPrincipal User user) {
		notificationService.setPushCommentOn(Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@DeleteMapping("/setting/comment")
	public ResponseEntity<CommonResponse> appPushCommentOff(@AuthenticationPrincipal User user) {
		notificationService.setPushCommentOff(Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@PostMapping("/setting/comment/like")
	public ResponseEntity<CommonResponse> appPushCommentLikeOn(@AuthenticationPrincipal User user) {
		notificationService.setPushCommentLikeOn(Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@DeleteMapping("/setting/comment/like")
	public ResponseEntity<CommonResponse> appPushCommentLikeOff(@AuthenticationPrincipal User user) {
		notificationService.setPushCommentLikeOff(Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@PostMapping("/check")
	public ResponseEntity<CommonResponse> checkNotification(
		@RequestBody NotificationCheckRequest notificationCheckRequest) {
		notificationService.checkNotification(notificationCheckRequest.getNotificationId());
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}
}
