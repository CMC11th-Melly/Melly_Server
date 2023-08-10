package cmc.mellyserver.mellyapi.comment.presentation;

import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.comment.presentation.dto.CommentAssembler;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentRequest;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentUpdateRequest;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.LikeRequest;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellycore.comment.application.CommentLikeService;
import cmc.mellyserver.mellycore.comment.application.CommentService;
import cmc.mellyserver.mellycore.comment.application.dto.response.CommentResponseDto;
import cmc.mellyserver.mellycore.common.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import static cmc.mellyserver.mellyapi.common.response.ApiResponse.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    private final CommentLikeService commentLikeService;

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse> removeCommentLike(@AuthenticationPrincipal User user, @PathVariable Long commentId) {

        commentLikeService.deleteCommentLike(commentId, Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstants.MESSAGE_SUCCESS));
    }

    @PostMapping("/like")
    public ResponseEntity<ApiResponse> saveCommentLike(@AuthenticationPrincipal User user, @RequestBody LikeRequest likeRequest) {

        commentLikeService.saveCommentLike(Long.parseLong(user.getUsername()), likeRequest.getCommentId());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstants.MESSAGE_SUCCESS));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveComment(@CurrentUser LoginUser loginUser, @RequestBody CommentRequest commentRequest) {

        commentService.saveComment(CommentAssembler.commentRequestDto(loginUser.getId(), commentRequest));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/memory/{memoryId}")
    public ResponseEntity<ApiResponse> getComment(@CurrentUser LoginUser loginUser, @PathVariable Long memoryId) {

        CommentResponseDto comment = commentService.getComments(loginUser.getId(), memoryId);
        return OK(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> removeComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstants.MESSAGE_SUCCESS));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {

        commentService.updateComment(commentId, commentUpdateRequest.getContent());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstants.MESSAGE_SUCCESS));
    }
}
