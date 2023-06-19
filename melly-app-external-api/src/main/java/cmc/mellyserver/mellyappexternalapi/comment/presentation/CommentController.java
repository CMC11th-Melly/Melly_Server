package cmc.mellyserver.mellyappexternalapi.comment.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.mellyappexternalapi.comment.application.dto.response.CommentResponseDto;
import cmc.mellyserver.mellyappexternalapi.comment.application.impl.CommentServiceImpl;
import cmc.mellyserver.mellyappexternalapi.comment.presentation.dto.CommentAssembler;
import cmc.mellyserver.mellyappexternalapi.comment.presentation.dto.request.CommentRequest;
import cmc.mellyserver.mellyappexternalapi.comment.presentation.dto.request.CommentUpdateRequest;
import cmc.mellyserver.mellyappexternalapi.comment.presentation.dto.request.LikeRequest;
import cmc.mellyserver.mellyappexternalapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyappexternalapi.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

	private final CommentServiceImpl commentService;

	@DeleteMapping("/{commentId}/like")
	public ResponseEntity<CommonResponse> removeCommentLike(@AuthenticationPrincipal User user,
		@PathVariable Long commentId) {
		commentService.deleteCommentLike(commentId, Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@PostMapping("/like")
	public ResponseEntity<CommonResponse> saveCommentLike(@AuthenticationPrincipal User user,
		@RequestBody LikeRequest likeRequest) {
		System.out.println("hello");
		commentService.saveCommentLike(Long.parseLong(user.getUsername()), likeRequest.getCommentId());
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@PostMapping
	public ResponseEntity<CommonResponse> saveComment(@AuthenticationPrincipal User user,
		@RequestBody CommentRequest commentRequest) {
		commentService.saveComment(
			CommentAssembler.commentRequestDto(Long.parseLong(user.getUsername()), commentRequest));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@GetMapping("/memory/{memoryId}")
	public ResponseEntity<CommonResponse> getComment(@AuthenticationPrincipal User user, @PathVariable Long memoryId) {
		CommentResponseDto comment = commentService.getComment(Long.parseLong(user.getUsername()), memoryId);
		return ResponseEntity.ok(new CommonResponse(200, "댓글 조회", comment));
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<CommonResponse> removeComment(@PathVariable Long commentId) {
		commentService.deleteComment(commentId);
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<CommonResponse> updateComment(@PathVariable Long commentId,
		@RequestBody CommentUpdateRequest commentUpdateRequest) {
		commentService.updateComment(commentId, commentUpdateRequest.getContent());
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}
}
