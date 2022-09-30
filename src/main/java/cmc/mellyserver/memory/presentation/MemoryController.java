package cmc.mellyserver.memory.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.memory.application.MemoryService;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.place.presentation.dto.PlaceInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoryController {

    private final MemoryService memoryService;

    @Operation(summary = "메모리 저장",description = "- 사용자가 메모리를 저장할때 장소 엔티티가 있으면 사용, 없으면 좌표 기준으로 장소 엔티티도 생성")
    @PostMapping("/memory")
    public ResponseEntity<CommonResponse> save(@AuthenticationPrincipal User user, @RequestBody PlaceInfoRequest placeInfoRequest)
    {
        Memory memory = memoryService.createMemory(user.getUsername(),placeInfoRequest);
        return ResponseEntity.ok(new CommonResponse(200,"메모리 저장 완료"));
    }
}
