package cmc.mellyserver.memory.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.application.MemoryService;
import cmc.mellyserver.memory.application.dto.MemoryForGroupResponse;
import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import cmc.mellyserver.memory.application.dto.MemoryUpdateFormResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.presentation.dto.request.MemorySearchDto;
import cmc.mellyserver.memory.presentation.dto.common.MemoryAssembler;
import cmc.mellyserver.memory.presentation.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.memory.presentation.dto.response.GetMemoryForPlaceResponse;
import cmc.mellyserver.memory.presentation.dto.response.GetOtherMemoryForPlaceResponse;
import cmc.mellyserver.memory.presentation.dto.wrapper.*;
import cmc.mellyserver.place.presentation.dto.PlaceInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memory")
public class MemoryController {

    private final MemoryService memoryService;


    @Operation(summary = "메모리 추가를 위한 로그인 유저의 그룹 조회")
    @GetMapping("/group")
    public ResponseEntity<CommonResponse> getUserGroupForMemoryForm(@AuthenticationPrincipal User user)
    {
        List<MemoryFormGroupResponse> userGroup = memoryService.getUserGroupForMemoryForm(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"메모리 작성 위한 그룹 정보 전달",new MemoryFormGroupResponseWrapper(userGroup)));
    }



    @Operation(summary = "내가 작성한 메모리 조회",description ="- 메모리 생성 날짜(연월일), 그룹 타입, 키워드로 필터링 가능" +
                                                             "\n- 연월일 데이터 보낼때는 20221010 형식으로 String 보내주시면 감사하겠습니다!")
    @GetMapping("/user/place/{placeId}")
    public ResponseEntity<CommonResponse> getUserMemory(@ParameterObject @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC,size = 10) Pageable pageable,
                                                        @AuthenticationPrincipal User user,
                                                        @PathVariable Long placeId,
                                                        @RequestParam(required = false) GroupType groupType)
    {
        Slice<GetMemoryForPlaceResponse> userMemory = memoryService.getUserMemory(pageable, user.getUsername(), placeId, groupType);
        return ResponseEntity.ok(new CommonResponse(200, "내가 작성한 메모리 전체 조회", new GetMemoryForPlaceResponseWrapper(userMemory.getContent().stream().count(),userMemory)));
    }



    /**
     * 메모리 차단 반영 완료
     */
    @Operation(summary = "다른 사람들이 전체 공개로 작성한 메모리 조회", description = "- 메모리 생성 날짜(연월일), 키워드로 필터링 가능" +
            "\n- 연월일 데이터 보낼때는 20221010 형식으로 String 보내주시면 감사하겠습니다!")
    @GetMapping("/other/place/{placeId}")
    public ResponseEntity<CommonResponse> getOtherMemory(@AuthenticationPrincipal User user,
                                                         @PathVariable Long placeId,
                                                         @RequestParam(required = false) GroupType groupType,
                                                         @ParameterObject @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC,size = 10) Pageable pageable)
    {
        Slice<GetOtherMemoryForPlaceResponse> otherMemory = memoryService.getOtherMemoryTemp(pageable, user.getUsername(), placeId, groupType);
        return ResponseEntity.ok(new CommonResponse(200, "성공", new GetOtherMemoryForPlaceResponseWrapper(otherMemory.stream().count(),otherMemory)));
    }


    /**
     * 메모리 차단 반영 완료
     */
    @Operation(summary = "내가 속해있는 그룹의 사람이 이 장소에 남긴 메모리 조회")
    @GetMapping("/group/place/{placeId}")
    public ResponseEntity<CommonResponse> getMyGroupMemory(           @AuthenticationPrincipal User user,
                                                                      @PathVariable Long placeId,
                                                                      @ParameterObject Pageable pageable,
                                                                      @RequestParam(required = false) GroupType groupType)
    {
        Slice<MemoryForGroupResponse> result = memoryService.getMyGroupMemory(pageable, user.getUsername(), placeId,groupType);
        return ResponseEntity.ok(new CommonResponse(200,"성공",new GetMyGroupMemoryForPlaceResponseWrapper(result.stream().count(),result)));
    }



    @Operation(summary = "메모리 저장",description = "- 사용자가 메모리를 저장할때 장소 엔티티가 있으면 사용, 없으면 좌표 기준으로 장소 엔티티도 생성")
    @PostMapping
    public ResponseEntity<CommonResponse> save(@AuthenticationPrincipal User user, @RequestPart(name = "images",required = false)  List<MultipartFile> images,
                                               @RequestPart(name = "memoryData") PlaceInfoRequest placeInfoRequest)
    {
        memoryService.createMemory(user.getUsername(), images, placeInfoRequest);
        return ResponseEntity.ok(new CommonResponse(200,"메모리 저장 완료"));
    }



    @Operation(summary = "메모리 수정(테스트 필요한 API 입니다)",description = "사용자가 작성한 메모리 수정")
    @PutMapping("/{memoryId}")
    public ResponseEntity<CommonResponse> updateMemory(@AuthenticationPrincipal User user, @PathVariable Long memoryId,@RequestPart(name = "images",required = false)  List<MultipartFile> images,
                                                       @RequestPart(name = "memoryData") MemoryUpdateRequest memoryUpdateRequest)
    {
         memoryService.updateMemory(user.getUsername(),memoryId,memoryUpdateRequest,images);
         return ResponseEntity.ok(new CommonResponse(200,"메모리 수정 완료"));
    }



    @Operation(summary = "메모리 수정을 위한 폼 데이터 전송")
    @GetMapping("/{memoryId}/update")
    public ResponseEntity<CommonResponse> getFormForUpdateMemory(@AuthenticationPrincipal User user, @PathVariable Long memoryId)
    {
        MemoryUpdateFormResponse formForUpdateMemory = memoryService.getFormForUpdateMemory(user.getUsername(), memoryId);
        return ResponseEntity.ok(new CommonResponse(200,"메모리 업데이트 수정 폼",new MemoryUpDateFormResponseWrapper(formForUpdateMemory)));
    }



    @Operation(summary = "메모리 삭제",description = "사용자가 작성한 메모리 삭제")
    @DeleteMapping("/{memoryId}")
    public ResponseEntity<CommonResponse> deleteMemory(@PathVariable Long memoryId)
    {
        memoryService.removeMemory(memoryId);
        return ResponseEntity.ok(new CommonResponse(200,"메세지 삭제 완료"));
    }


    /**
     * 내가 작성한 메모리 중 고르는 거라서 메모리 차단 설정 필요 없음
     */
    @Operation(summary = "검색창에서 메모리 제목으로 검색")
    @GetMapping("/search")
    public ResponseEntity<CommonResponse> searchPlaceByMemoryTitle(@AuthenticationPrincipal User user, @RequestParam String memoryName)
    {
        List<MemorySearchDto> result = memoryService.searchPlaceByMemoryTitle(user.getUsername(), memoryName);
        return ResponseEntity.ok(new CommonResponse(200,"메모리 제목 검색",new SearchMemoryNameResponseWrapper(result)));
    }


}
