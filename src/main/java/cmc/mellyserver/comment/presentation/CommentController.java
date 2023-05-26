package cmc.mellyserver.comment.presentation;

import cmc.mellyserver.comment.application.CommentService;
import cmc.mellyserver.comment.application.dto.CommentResponseDto;
import cmc.mellyserver.comment.presentation.dto.CommentRequest;
import cmc.mellyserver.comment.presentation.dto.CommentUpdateRequest;
import cmc.mellyserver.comment.presentation.dto.LikeRequest;
import cmc.mellyserver.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/like")
    public ResponseEntity<CommonResponse> saveCommentLike(@AuthenticationPrincipal User user, @RequestBody LikeRequest likeRequest)
    {
        commentService.saveCommentLike(Long.parseLong(user.getUsername()),likeRequest.getCommentId());
        return ResponseEntity.ok(new CommonResponse(200,"댓글에 좋아요 추가 완료"));
    }


    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<CommonResponse> saveCommentLike(@AuthenticationPrincipal User user, @PathVariable Long commentId)
    {
        commentService.deleteCommentLike(commentId,Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(200,"댓글에 좋아요 삭제 완료"));
    }


    @PostMapping
    public ResponseEntity<CommonResponse> saveComment(@AuthenticationPrincipal User user, @RequestBody CommentRequest commentRequest)
    {
         commentService.saveComment(Long.parseLong(user.getUsername()),commentRequest);
         return ResponseEntity.ok(new CommonResponse(200,"댓글 추가 완료"));
    }



    @GetMapping("/memory/{memoryId}")
    public ResponseEntity<CommonResponse> getComment(@AuthenticationPrincipal User user,@PathVariable Long memoryId)
    {
        CommentResponseDto comment = commentService.getComment(Long.parseLong(user.getUsername()),memoryId);
        return ResponseEntity.ok(new CommonResponse(200,"댓글 조회",comment));
    }



    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse> removeComment(@PathVariable Long commentId)
    {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new CommonResponse(200,"댓글 삭제 완료"));
    }



    @PutMapping("/{commentId}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest)
    {
        commentService.updateComment(commentId,commentUpdateRequest.getContent());
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }
}
