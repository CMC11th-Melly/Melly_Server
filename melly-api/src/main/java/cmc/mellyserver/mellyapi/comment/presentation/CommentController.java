package cmc.mellyserver.mellyapi.comment.presentation;

import cmc.mellyserver.mellyapi.comment.presentation.dto.CommentAssembler;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentRequest;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentUpdateRequest;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.LikeRequest;
import cmc.mellyserver.mellyapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellycore.comment.application.CommentLikeService;
import cmc.mellyserver.mellycore.comment.application.CommentService;
import cmc.mellyserver.mellycore.comment.application.dto.response.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    private final CommentLikeService commentLikeService;

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse> removeCommentLike(@AuthenticationPrincipal User user, @PathVariable Long commentId) {

        commentLikeService.deleteCommentLike(commentId, Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PostMapping("/like")
    public ResponseEntity<ApiResponse> saveCommentLike(@AuthenticationPrincipal User user, @RequestBody LikeRequest likeRequest) {

        commentLikeService.saveCommentLike(Long.parseLong(user.getUsername()), likeRequest.getCommentId());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveComment(@AuthenticationPrincipal User user, @RequestBody CommentRequest commentRequest) {

        commentService.saveComment(CommentAssembler.commentRequestDto(Long.parseLong(user.getUsername()), commentRequest));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @GetMapping("/memory/{memoryId}")
    public ResponseEntity<ApiResponse> getComment(@AuthenticationPrincipal User user, @PathVariable Long memoryId) {

        CommentResponseDto comment = commentService.getComment(Long.parseLong(user.getUsername()), memoryId);
        return ResponseEntity.ok(new ApiResponse(200, "댓글 조회", comment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> removeComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {

        commentService.updateComment(commentId, commentUpdateRequest.getContent());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }
}
