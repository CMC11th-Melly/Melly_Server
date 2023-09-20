package cmc.mellyserver.mellyapi.comment.presentation;

import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.comment.presentation.dto.CommentAssembler;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentRequest;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentUpdateRequest;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.LikeRequest;
import cmc.mellyserver.mellyapi.common.code.SuccessCode;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellycore.comment.application.CommentLikeService;
import cmc.mellyserver.mellycore.comment.application.CommentService;
import cmc.mellyserver.mellycore.comment.application.dto.response.CommentResponseDto;
import cmc.mellyserver.mellycore.common.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    private final CommentLikeService commentLikeService;

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<Void>> removeCommentLike(@AuthenticationPrincipal User user, @PathVariable Long commentId) {

        commentLikeService.deleteCommentLike(commentId, Long.parseLong(user.getUsername()));
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @PostMapping("/like")
    public ResponseEntity<ApiResponse<Void>> saveCommentLike(@AuthenticationPrincipal User user, @RequestBody LikeRequest likeRequest) {

        commentLikeService.saveCommentLike(Long.parseLong(user.getUsername()), likeRequest.getCommentId());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveComment(@CurrentUser LoginUser loginUser, @RequestBody CommentRequest commentRequest) {

        commentService.saveComment(CommentAssembler.commentRequestDto(loginUser.getId(), commentRequest));
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @GetMapping("/memory/{memoryId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> getComment(@CurrentUser LoginUser loginUser, @PathVariable Long memoryId) {

        CommentResponseDto comment = commentService.getComments(loginUser.getId(), memoryId);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> removeComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {

        commentService.updateComment(commentId, commentUpdateRequest.getContent());
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }
}
