package cmc.mellyserver.memoryScrap.presentation;


import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.memoryScrap.application.MemoryScrapService;
import cmc.mellyserver.memoryScrap.presentation.dto.MemoryScrapRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoryScrapController {

    private final MemoryScrapService memoryScrapService;

    @Operation(summary = "메모리 스크랩")
    @PostMapping("/memory/scrap")
    public ResponseEntity<CommonResponse> scrapMemory(@AuthenticationPrincipal User user, @RequestBody MemoryScrapRequest memoryScrapRequest) {
        memoryScrapService.createScrap(user.getUsername(), memoryScrapRequest);
        return ResponseEntity.ok(new CommonResponse(200, "메모리 스크랩 완료"));
    }


    @Operation(summary = "메모리 스크랩 취소")
    @DeleteMapping("/memory/{memoryId}/scrap")
    public ResponseEntity<CommonResponse> removeScrap(@AuthenticationPrincipal User user,@PathVariable Long memoryId) {
        memoryScrapService.removeScrap(user.getUsername(), memoryId);
        return ResponseEntity.ok(new CommonResponse(200, "스크랩 삭제 완료"));
    }
}