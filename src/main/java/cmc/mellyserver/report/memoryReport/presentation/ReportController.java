package cmc.mellyserver.report.memoryReport.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.report.memoryReport.application.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "메모리 신고")
    @PostMapping("/memory/{memoryId}")
    public ResponseEntity<CommonResponse> reportMemory(@AuthenticationPrincipal User user, @PathVariable Long memoryId)
    {
         reportService.reportMemory(user.getUsername(),memoryId);
         return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }


//    @Operation(summary = "댓글 신고(만들어야 하는 API인지 더 체크 필요)")
//    @PostMapping("/comment/{commentId}")
//    public ResponseEntity<CommonResponse> reportComment(@AuthenticationPrincipal User user, @PathVariable Long commentId)
//    {
//        reportService.reportComment(user.getUsername(),commentId);
//        return ResponseEntity.ok(new CommonResponse(200,"성공"));
//    }


}
