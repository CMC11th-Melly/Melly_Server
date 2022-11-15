package cmc.mellyserver.block.memoryBlock.presentation;

import cmc.mellyserver.block.memoryBlock.application.MemoryBlockService;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.report.memoryReport.presentation.dto.MemoryReportRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/block/memory")
public class MemoryBlockController {

    private final MemoryBlockService memoryBlockService;

    @Operation(summary = "메모리 차단")
    @PostMapping
    public ResponseEntity<CommonResponse> blockMemory(@AuthenticationPrincipal User user, @RequestBody MemoryReportRequest memoryReportRequest)
    {
         memoryBlockService.blockMemory(user.getUsername(), memoryReportRequest.getMemoryId());
         return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }


}
