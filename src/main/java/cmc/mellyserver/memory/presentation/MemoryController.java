package cmc.mellyserver.memory.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.memory.application.MemoryService;
import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemorySearchDto;
import cmc.mellyserver.memory.presentation.dto.KeywordRequest;
import cmc.mellyserver.memory.presentation.dto.MemoryFormGroupResponseWrapper;
import cmc.mellyserver.memory.presentation.dto.SearchMemoryNameResponseWrapper;
import cmc.mellyserver.place.domain.service.dto.MyMemoryDto;
import cmc.mellyserver.place.presentation.dto.PlaceInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoryController {

    private final MemoryService memoryService;

    @Operation(summary = "메모리 추가를 위한 로그인 유저의 그룹 조회")
    @GetMapping("/memory/group")
    public ResponseEntity<CommonResponse> getUserGroup(@AuthenticationPrincipal User user)
    {
        List<MemoryFormGroupResponse> userGroup = memoryService.getUserGroup(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"메모리 작성 위한 그룹 정보 전달",new MemoryFormGroupResponseWrapper(userGroup)));
    }


    @Operation(summary = "메모리 저장",description = "- 사용자가 메모리를 저장할때 장소 엔티티가 있으면 사용, 없으면 좌표 기준으로 장소 엔티티도 생성")
    @PostMapping("/memory")
    public ResponseEntity<CommonResponse> save(@AuthenticationPrincipal User user, @RequestPart(name = "images",required = false) List<MultipartFile> images,@RequestPart(name = "memoryRequest") PlaceInfoRequest placeInfoRequest)
    {
        memoryService.createMemory(user.getUsername(),images,placeInfoRequest);
        return ResponseEntity.ok(new CommonResponse(200,"메모리 저장 완료"));
    }

//    @Operation(summary = "메모리 검색")
//    @GetMapping("/memory/search")
//    public void memorySearch(@AuthenticationPrincipal User user, @RequestParam String title)
//    {
//        memoryService.search(user.getUsername(),title);
//    }


    @Operation(summary = "메모리 검색")
    @GetMapping("/memory/search")
    public ResponseEntity<CommonResponse> memorySearch(@AuthenticationPrincipal User user, @RequestParam String memoryName)
    {
        List<MemorySearchDto> result = memoryService.searchMemory(user.getUsername(), memoryName);
        return ResponseEntity.ok(new CommonResponse(200,"메모리 제목 검색",new SearchMemoryNameResponseWrapper(result)));
    }




}
