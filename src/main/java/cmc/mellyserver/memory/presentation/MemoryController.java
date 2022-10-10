package cmc.mellyserver.memory.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.memory.application.MemoryService;
import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemorySearchDto;
import cmc.mellyserver.memory.presentation.dto.*;
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

    @Operation(summary = "내가 작성한 메모리 조회",description ="- 메모리 생성 날짜(연월일), 그룹 타입, 키워드로 필터링 가능" +
                                                             "\n- 연월일 데이터 보낼때는 20221010 형식으로 String 보내주시면 감사하겠습니다!")
    @GetMapping("/memory/user/place/{placeId}")
    public ResponseEntity<CommonResponse> getUserMemory(@AuthenticationPrincipal User user,
                              @PathVariable Long placeId,
                              GetUserMemoryCond getUserMemoryCond)
    {
        List<Memory> result = memoryService.getUserMemory(user.getUsername(), placeId, getUserMemoryCond);
        return ResponseEntity.ok(new CommonResponse(200, "특정 장소의 메모리 반환",
                new GetMemoryForPlaceResponseWrapper(MemoryAssembler.getMemoryForPlaceResponse(result))));
    }


    @Operation(summary = "다른 사람들이 전체 공개로 작성한 메모리 조회", description = "- 메모리 생성 날짜(연월일), 키워드로 필터링 가능" +
            "\n- 연월일 데이터 보낼때는 20221010 형식으로 String 보내주시면 감사하겠습니다!")
    @GetMapping("/memory/other/place/{placeId}")
    public ResponseEntity<CommonResponse> getOtherMemory(@AuthenticationPrincipal User user,
                               @PathVariable Long placeId,
                               GetOtherMemoryCond getOtherMemoryCond)
    {
        List<Memory> result = memoryService.getOtherMemory(user.getUsername(),
                placeId,
                getOtherMemoryCond);
        return ResponseEntity.ok(new CommonResponse(200, "특정 장소의 메모리 반환",
                new GetMemoryForPlaceResponseWrapper(MemoryAssembler.getMemoryForPlaceResponse(result))));
    }


    @Operation(summary = "메모리 저장",description = "- 사용자가 메모리를 저장할때 장소 엔티티가 있으면 사용, 없으면 좌표 기준으로 장소 엔티티도 생성")
    @PostMapping("/memory")
    public ResponseEntity<CommonResponse> save(@AuthenticationPrincipal User user,PlaceInfoRequest placeInfoRequest)
    {
        //  @RequestPart(name = "images",required = false) List<MultipartFile> images,
        System.out.println(placeInfoRequest.getKeyword());
        for(String k : placeInfoRequest.getKeyword())
        {
            System.out.println(k);
        }
        memoryService.createMemory(user.getUsername(),placeInfoRequest.getImages(),placeInfoRequest);
        return ResponseEntity.ok(new CommonResponse(200,"메모리 저장 완료"));
    }


    @Operation(summary = "검색창에서 메모리 제목으로 검색")
    @GetMapping("/memory/search")
    public ResponseEntity<CommonResponse> memorySearch(@AuthenticationPrincipal User user, @RequestParam String memoryName)
    {
        List<MemorySearchDto> result = memoryService.searchMemory(user.getUsername(), memoryName);
        return ResponseEntity.ok(new CommonResponse(200,"메모리 제목 검색",new SearchMemoryNameResponseWrapper(result)));
    }




}
