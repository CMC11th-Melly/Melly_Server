package cmc.mellyserver.mellyapi.memory.presentation;

import cmc.mellyserver.mellyapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyapi.common.response.CommonResponse;
import cmc.mellyserver.mellyapi.memory.application.MemoryService;
import cmc.mellyserver.mellyapi.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.MemoryAssembler;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper.GetMemoryForPlaceResponseWrapper;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper.GetMyGroupMemoryForPlaceResponseWrapper;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper.GetOtherMemoryForPlaceResponseWrapper;
import cmc.mellyserver.mellyapi.memory.presentation.dto.request.MemoryCreateRequest;
import cmc.mellyserver.mellyapi.memory.presentation.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.mellyapi.memory.presentation.dto.response.MemoryResponse;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/group")
    public ResponseEntity<CommonResponse> getGroupListForSaveMemory(@AuthenticationPrincipal User user) {

        List<GroupListForSaveMemoryResponseDto> userGroup = memoryService.findGroupListLoginUserParticipate(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                MemoryAssembler.groupListForSaveMemoryResponse(userGroup)));
    }

    @GetMapping("/user/place/{placeId}")
    public ResponseEntity<CommonResponse> getUserMemory(@PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
                                                        @AuthenticationPrincipal User user, @PathVariable Long placeId, @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> userMemory = memoryService.findLoginUserWriteMemoryBelongToPlace(pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(userMemory);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new GetMemoryForPlaceResponseWrapper(memoryResponses.getContent().stream().count(), memoryResponses)));
    }

    @GetMapping("/other/place/{placeId}")
    public ResponseEntity<CommonResponse> getOtherMemory(@AuthenticationPrincipal User user,
                                                         @PathVariable Long placeId,
                                                         @RequestParam(required = false) GroupType groupType,
                                                         @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Slice<MemoryResponseDto> otherMemory = memoryService.findOtherUserWriteMemoryBelongToPlace(
                pageable,
                Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(otherMemory);
        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                        new GetOtherMemoryForPlaceResponseWrapper(memoryResponses.stream().count(),
                                memoryResponses)));
    }

    @GetMapping("/group/place/{placeId}")
    public ResponseEntity<CommonResponse> getMyGroupMemory(@AuthenticationPrincipal User user,
                                                           @PathVariable Long placeId, Pageable pageable,
                                                           @RequestParam(required = false) GroupType groupType) {
        Slice<MemoryResponseDto> results = memoryService.findMyGroupMemberWriteMemoryBelongToPlace(
                pageable,
                Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(results);
        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                        new GetMyGroupMemoryForPlaceResponseWrapper(
                                memoryResponses.stream().count(), memoryResponses)));
    }

    @PostMapping
    public ResponseEntity<CommonResponse> save(@AuthenticationPrincipal User user,
                                               @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                               @Valid @RequestPart(name = "memoryData") MemoryCreateRequest memoryCreateRequest) {
        memoryService.createMemory(
                MemoryAssembler.createMemoryRequestDto(Long.parseLong(user.getUsername()), images,
                        memoryCreateRequest));
        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PutMapping("/{memoryId}")
    public ResponseEntity<CommonResponse> updateMemory(@AuthenticationPrincipal User user,
                                                       @PathVariable Long memoryId,
                                                       @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                                       @RequestPart(name = "memoryData") MemoryUpdateRequest memoryUpdateRequest) {
        memoryService.updateMemory(
                MemoryAssembler.updateMemoryRequestDto(Long.parseLong(user.getUsername()), memoryId,
                        memoryUpdateRequest,
                        images));
        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @GetMapping("/{memoryId}/update")
    public ResponseEntity<CommonResponse> getFormForUpdateMemory(@AuthenticationPrincipal User user,
                                                                 @PathVariable Long memoryId) {
        MemoryUpdateFormResponseDto formForUpdateMemory = memoryService.findMemoryUpdateFormData(
                Long.parseLong(user.getUsername()), memoryId);
        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                        MemoryAssembler.memoryUpdateFormResponse(formForUpdateMemory)));
    }

    @DeleteMapping("/{memoryId}")
    public ResponseEntity<CommonResponse> deleteMemory(@AuthenticationPrincipal User user,
                                                       @PathVariable Long memoryId) {
        memoryService.removeMemory(Long.parseLong(user.getUsername()), memoryId);
        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @GetMapping("/{memoryId}")
    public ResponseEntity<CommonResponse> findMemory(@AuthenticationPrincipal User user,
                                                     @PathVariable Long memoryId) {
        MemoryResponseDto memoryByMemoryId = memoryService.findMemoryByMemoryId(
                Long.parseLong(user.getUsername()),
                memoryId);

        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                        MemoryAssembler.memoryResponse(memoryByMemoryId)));
    }


    @GetMapping("/search")
    public ResponseEntity<CommonResponse> searchPlaceByMemoryTitle(
            @AuthenticationPrincipal User user,
            @RequestParam String memoryName) {
        List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryNameResponseDtos = memoryService.findPlaceInfoByMemoryName(
                Long.parseLong(user.getUsername()), memoryName);
        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                        MemoryAssembler.findPlaceInfoByMemoryNameResponses(
                                findPlaceInfoByMemoryNameResponseDtos)));
    }
}
