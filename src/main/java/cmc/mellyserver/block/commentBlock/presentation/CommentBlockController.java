package cmc.mellyserver.block.commentBlock.presentation;

import cmc.mellyserver.block.commentBlock.application.CommentBlockService;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.report.commentReport.application.CommentReportService;
import cmc.mellyserver.report.commentReport.presentation.dto.CommentReportRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/block/comment")
public class CommentBlockController {

    private final CommentBlockService commentBlockService;

    @Operation(summary = "댓글 차단")
    @PostMapping
    public ResponseEntity<CommonResponse> createCommentReport(@AuthenticationPrincipal User user, @RequestBody CommentReportRequest commentReportRequest)
    {
        commentBlockService.commentBlock(user.getUsername(),commentReportRequest.getCommentId());
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }
}
