package cmc.mellyserver.controller.comment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.auth.controller.dto.common.CurrentUser;
import cmc.mellyserver.auth.controller.dto.common.LoginUser;
import cmc.mellyserver.controller.comment.dto.CommentAssembler;
import cmc.mellyserver.controller.comment.dto.request.CommentRequest;
import cmc.mellyserver.controller.comment.dto.request.CommentUpdateRequest;
import cmc.mellyserver.controller.comment.dto.request.LikeRequest;
import cmc.mellyserver.domain.comment.CommentLikeService;
import cmc.mellyserver.domain.comment.CommentService;
import cmc.mellyserver.domain.comment.dto.response.CommentResponseDto;
import cmc.mellyserver.support.response.ApiResponse;
import cmc.mellyserver.support.response.SuccessCode;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

  private final CommentService commentService;

  private final CommentLikeService commentLikeService;

  @DeleteMapping("/{commentId}/like")
  public ResponseEntity<ApiResponse<Void>> removeCommentLike(@CurrentUser LoginUser loginUser,
	  @PathVariable Long commentId) {

	commentLikeService.deleteCommentLike(loginUser.getId(), commentId);
	return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
  }

  @PostMapping("/like")
  public ResponseEntity<ApiResponse<Void>> saveCommentLike(@CurrentUser LoginUser loginUser,
	  @RequestBody LikeRequest likeRequest) {

	commentLikeService.saveCommentLike(loginUser.getId(), likeRequest.getCommentId());
	return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveComment(@CurrentUser LoginUser loginUser,
	  @RequestBody CommentRequest commentRequest) {

	commentService.saveComment(CommentAssembler.commentRequestDto(loginUser.getId(), commentRequest));
	return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
  }

  @GetMapping("/memories/{memoryId}")
  public ResponseEntity<ApiResponse<CommentResponseDto>> getComments(@CurrentUser LoginUser loginUser,
	  @PathVariable Long memoryId) {

	CommentResponseDto comment = commentService.getComments(loginUser.getId(), memoryId);
	return ApiResponse.success(SuccessCode.SELECT_SUCCESS, comment);
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<ApiResponse<Void>> removeComment(@PathVariable Long commentId) {

	commentService.deleteComment(commentId);
	return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
  }

  @PatchMapping("/{commentId}")
  public ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable Long commentId,
	  @RequestBody CommentUpdateRequest commentUpdateRequest) {

	commentService.updateComment(commentId, commentUpdateRequest.getContent());
	return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
  }

}
