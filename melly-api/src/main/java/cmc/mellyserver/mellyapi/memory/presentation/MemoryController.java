package cmc.mellyserver.mellyapi.memory.presentation;

import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.MemoryAssembler;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper.GetMemoryForPlaceResponseWrapper;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper.GetMyGroupMemoryForPlaceResponseWrapper;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper.GetOtherMemoryForPlaceResponseWrapper;
import cmc.mellyserver.mellyapi.memory.presentation.dto.request.MemoryCreateRequest;
import cmc.mellyserver.mellyapi.memory.presentation.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.mellyapi.memory.presentation.dto.response.MemoryResponse;
import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.group.application.GroupService;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.application.MemoryReadService;
import cmc.mellyserver.mellycore.memory.application.MemoryWriteService;
import cmc.mellyserver.mellycore.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.CREATED;
import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.NO_CONTENT;
import static cmc.mellyserver.mellyapi.common.response.ApiResponse.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memorys")
public class MemoryController {

    private final MemoryReadService memoryReadService;

    private final MemoryWriteService memoryWriteService;

    private final GroupService groupService;

    @GetMapping("/group")
    public ResponseEntity<ApiResponse> getGroupListForSaveMemory(@AuthenticationPrincipal User user) {

        List<GroupLoginUserParticipatedResponseDto> userGroup = groupService.findGroupListLoginUserParticipateForMemoryCreate(Long.parseLong(user.getUsername()));
        return OK(MemoryAssembler.groupListForSaveMemoryResponse(userGroup));
    }

    @GetMapping("/user/place/{placeId}")
    public ResponseEntity<ApiResponse> getUserMemory(
            @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @AuthenticationPrincipal User user, @PathVariable Long placeId,
            @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> userMemory = memoryReadService.findLoginUserWriteMemoryBelongToPlace(pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(userMemory);
        return OK(new GetMemoryForPlaceResponseWrapper(memoryResponses.getContent().stream().count(), memoryResponses));
    }

    @GetMapping("/other/place/{placeId}")
    public ResponseEntity<ApiResponse> getOtherMemory(@AuthenticationPrincipal User user,
                                                      @PathVariable Long placeId, @RequestParam(required = false) GroupType groupType,
                                                      @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {

        Slice<MemoryResponseDto> otherMemory = memoryReadService.findOtherUserWriteMemoryBelongToPlace(
                pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(otherMemory);
        return OK(new GetOtherMemoryForPlaceResponseWrapper(memoryResponses.stream().count(), memoryResponses));
    }

    @GetMapping("/group/place/{placeId}")
    public ResponseEntity<ApiResponse> getMyGroupMemory(@AuthenticationPrincipal User user,
                                                        @PathVariable Long placeId, Pageable pageable,
                                                        @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> results = memoryReadService.findMyGroupMemberWriteMemoryBelongToPlace(
                pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(results);
        return OK(new GetMyGroupMemoryForPlaceResponseWrapper(memoryResponses.stream().count(), memoryResponses));
    }

    @PostMapping
    public ResponseEntity<Void> save(@AuthenticationPrincipal User user,
                                     @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                     @Valid @RequestPart(name = "memoryData") MemoryCreateRequest memoryCreateRequest) {

        memoryWriteService.createMemory(MemoryAssembler.createMemoryRequestDto(Long.parseLong(user.getUsername()), images, memoryCreateRequest));
        return CREATED;
    }

    @PutMapping("/{memoryId}")
    public ResponseEntity<Void> updateMemory(@AuthenticationPrincipal User user,
                                             @PathVariable Long memoryId,
                                             @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                             @RequestPart(name = "memoryData") MemoryUpdateRequest memoryUpdateRequest) {

        memoryWriteService.updateMemory(MemoryAssembler.updateMemoryRequestDto(Long.parseLong(user.getUsername()), memoryId, memoryUpdateRequest, images));
        return NO_CONTENT;
    }

    @GetMapping("/{memoryId}/update")
    public ResponseEntity<ApiResponse> getFormForUpdateMemory(@AuthenticationPrincipal User user, @PathVariable Long memoryId) {

        MemoryUpdateFormResponseDto formForUpdateMemory = memoryReadService.findMemoryUpdateFormData(Long.parseLong(user.getUsername()), memoryId);
        return OK(MemoryAssembler.memoryUpdateFormResponse(formForUpdateMemory));
    }

    @DeleteMapping("/{memoryId}")
    public ResponseEntity<Void> deleteMemory(@PathVariable Long memoryId) {

        memoryWriteService.removeMemory(memoryId);
        return NO_CONTENT;
    }


    @GetMapping("/{memoryId}")
    public ResponseEntity<ApiResponse> findMemory(@AuthenticationPrincipal User user, @PathVariable Long memoryId) {

        MemoryResponseDto memoryByMemoryId = memoryReadService.findMemoryByMemoryId(Long.parseLong(user.getUsername()), memoryId);
        return OK(MemoryResponse.of(memoryByMemoryId));
    }
}
