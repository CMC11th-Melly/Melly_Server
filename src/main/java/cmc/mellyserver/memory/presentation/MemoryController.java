package cmc.mellyserver.memory.presentation;

import cmc.mellyserver.common.constants.MessageConstant;
import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.application.MemoryService;
import cmc.mellyserver.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.memory.application.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.memory.application.dto.response.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.memory.presentation.dto.common.MemoryAssembler;
import cmc.mellyserver.memory.application.dto.response.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.memory.presentation.dto.common.wrapper.GetMemoryForPlaceResponseWrapper;
import cmc.mellyserver.memory.presentation.dto.common.wrapper.GetMyGroupMemoryForPlaceResponseWrapper;
import cmc.mellyserver.memory.presentation.dto.common.wrapper.GetOtherMemoryForPlaceResponseWrapper;
import cmc.mellyserver.memory.presentation.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.memory.presentation.dto.response.MemoryResponse;
import cmc.mellyserver.place.presentation.dto.request.PlaceInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memory")
public class MemoryController {

    private final MemoryService memoryService;


    @Operation(summary = "메모리 추가를 위한 로그인 유저의 그룹 조회")
    @GetMapping("/group")
    public ResponseEntity<CommonResponse> getGroupListForSaveMemory(@AuthenticationPrincipal User user)
    {
        List<GroupListForSaveMemoryResponseDto> userGroup = memoryService.findGroupListLoginUserParticipate(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new CommonDetailResponse<>(MemoryAssembler.groupListForSaveMemoryResponse(userGroup))));
    }


    @Operation(summary = "내가 이 장소에 작성한 메모리 조회")
    @GetMapping("/user/place/{placeId}")
    public ResponseEntity<CommonResponse> getUserMemory(@ParameterObject @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC,size = 10) Pageable pageable,
                                                        @AuthenticationPrincipal User user, @PathVariable Long placeId, @RequestParam(required = false) GroupType groupType)
    {
        Slice<MemoryResponseDto> userMemory = memoryService.findLoginUserWriteMemoryBelongToPlace(pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(userMemory);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new CommonDetailResponse<>(new GetMemoryForPlaceResponseWrapper(memoryResponses.getContent().stream().count(),memoryResponses))));
    }


    @Operation(summary = "다른 사람들이 이 장소에 전체 공개로 작성한 메모리 조회")
    @GetMapping("/other/place/{placeId}")
    public ResponseEntity<CommonResponse> getOtherMemory(@AuthenticationPrincipal User user, @PathVariable Long placeId, @RequestParam(required = false) GroupType groupType,
                                                         @ParameterObject @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC,size = 10) Pageable pageable)
    {
        Slice<MemoryResponseDto> otherMemory = memoryService.findOtherUserWriteMemoryBelongToPlace(pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(otherMemory);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new CommonDetailResponse<>(new GetOtherMemoryForPlaceResponseWrapper(memoryResponses.stream().count(),memoryResponses))));
    }



    @Operation(summary = "내가 속해있는 그룹의 사람이 이 장소에 남긴 메모리 조회")
    @GetMapping("/group/place/{placeId}")
    public ResponseEntity<CommonResponse> getMyGroupMemory(@AuthenticationPrincipal User user, @PathVariable Long placeId, @ParameterObject Pageable pageable, @RequestParam(required = false) GroupType groupType)
    {
        Slice<MemoryResponseDto> results = memoryService.findMyGroupMemberWriteMemoryBelongToPlace(pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(results);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(new GetMyGroupMemoryForPlaceResponseWrapper(memoryResponses.stream().count(),memoryResponses))));
    }


    @Operation(summary = "메모리 저장")
    @PostMapping
    public ResponseEntity<CommonResponse> save(@AuthenticationPrincipal User user, @RequestPart(name = "images",required = false)  List<MultipartFile> images, @Valid @RequestPart(name = "memoryData") PlaceInfoRequest placeInfoRequest)
    {
        memoryService.createMemory(CreateMemoryRequestDto.of(Long.parseLong(user.getUsername()),images,placeInfoRequest));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @Operation(summary = "메모리 수정(테스트 필요한 API 입니다)")
    @PutMapping("/{memoryId}")
    public ResponseEntity<CommonResponse> updateMemory(@AuthenticationPrincipal User user, @PathVariable Long memoryId, @RequestPart(name = "images",required = false)  List<MultipartFile> images, @RequestPart(name = "memoryData") MemoryUpdateRequest memoryUpdateRequest)
    {
         memoryService.updateMemory(UpdateMemoryRequestDto.of(Long.parseLong(user.getUsername()),memoryId,memoryUpdateRequest,images));
         return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @Operation(summary = "메모리 수정을 위한 폼 데이터 전송")
    @GetMapping("/{memoryId}/update")
    public ResponseEntity<CommonResponse> getFormForUpdateMemory(@AuthenticationPrincipal User user, @PathVariable Long memoryId)
    {
        MemoryUpdateFormResponseDto formForUpdateMemory = memoryService.findMemoryUpdateFormData(Long.parseLong(user.getUsername()), memoryId);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(MemoryAssembler.memoryUpdateFormResponse(formForUpdateMemory))));
    }


    @Operation(summary = "메모리 삭제",description = "사용자가 작성한 메모리 삭제")
    @DeleteMapping("/{memoryId}")
    public ResponseEntity<CommonResponse> deleteMemory(@PathVariable Long memoryId)
    {
        memoryService.removeMemory(memoryId);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @Operation(summary = "메모리 id로 메모리 조회")
    @GetMapping("/{memoryId}")
    public ResponseEntity<CommonResponse> findMemory(@AuthenticationPrincipal User user, @PathVariable Long memoryId)
    {
        MemoryResponseDto memoryByMemoryId = memoryService.findMemoryByMemoryId(Long.parseLong(user.getUsername()), memoryId);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(MemoryAssembler.memoryResponse(memoryByMemoryId))));
    }


    @Operation(summary = "검색창에서 포함하고 있는 메모리 제목으로 장소 검색")
    @GetMapping("/search")
    public ResponseEntity<CommonResponse> searchPlaceByMemoryTitle(@AuthenticationPrincipal User user, @RequestParam String memoryName)
    {
        List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryNameResponseDtos = memoryService.findPlaceInfoByMemoryName(Long.parseLong(user.getUsername()), memoryName);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(MemoryAssembler.findPlaceInfoByMemoryNameResponses(findPlaceInfoByMemoryNameResponseDtos))));
    }
}
