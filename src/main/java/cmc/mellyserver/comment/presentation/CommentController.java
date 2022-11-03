package cmc.mellyserver.comment.presentation;

import cmc.mellyserver.comment.application.CommentService;
import cmc.mellyserver.comment.application.dto.CommentDto;
import cmc.mellyserver.comment.application.dto.CommentResponseDto;
import cmc.mellyserver.comment.presentation.dto.CommentRequest;
import cmc.mellyserver.comment.presentation.dto.CommentUpdateRequest;
import cmc.mellyserver.comment.presentation.dto.LikeRequest;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.notification.application.FCMService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final FCMService fcmService;
    @Operation(summary = "댓글에 좋아요 추가")
    @PostMapping("/like")
    public ResponseEntity<CommonResponse> saveCommentLike(@AuthenticationPrincipal User user, @RequestBody LikeRequest likeRequest)
    {
             commentService.saveCommentLike(user.getUsername(),likeRequest.getCommentId());
             return ResponseEntity.ok(new CommonResponse(200,"댓글에 좋아요 추가 완료"));
    }


    @Operation(summary = "댓글 좋아요 삭제")
    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<CommonResponse> saveCommentLike(@AuthenticationPrincipal User user, @PathVariable Long commentId)
    {
        commentService.deleteCommentLike(commentId,user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"댓글에 좋아요 삭제 완료"));
    }


    @Operation(summary = "댓글 달기")
    @PostMapping
    public ResponseEntity<CommonResponse> saveComment(@AuthenticationPrincipal User user, @RequestBody CommentRequest commentRequest)
    {
         commentService.saveComment(user.getUsername(),commentRequest);
         return ResponseEntity.ok(new CommonResponse(200,"댓글 추가 완료"));
    }


    @Operation(summary = "댓글 조회")
    @GetMapping("/memory/{memoryId}")
    public ResponseEntity<CommonResponse> getComment(@AuthenticationPrincipal User user,@PathVariable Long memoryId)
    {
        CommentResponseDto comment = commentService.getComment(user.getUsername(),memoryId);
        return ResponseEntity.ok(new CommonResponse(200,"댓글 조회",comment));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse> removeComment(@PathVariable Long commentId)
    {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new CommonResponse(200,"댓글 삭제 완료"));
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest)
    {
        commentService.updateComment(commentId,commentUpdateRequest.getContent());
        return ResponseEntity.ok(new CommonResponse(200,"댓글 수정 완료"));
    }

}
