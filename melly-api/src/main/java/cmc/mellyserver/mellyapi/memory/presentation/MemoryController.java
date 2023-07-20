package cmc.mellyserver.mellyapi.memory.presentation;

import cmc.mellyserver.mellyapi.common.constants.MessageConstant;
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
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memorys")
public class MemoryController {

    private final MemoryReadService memoryReadService;

    private final MemoryWriteService memoryWriteService;

    private final GroupService groupService;

    @GetMapping("/group")
    public ResponseEntity<ApiResponse> getGroupListForSaveMemory(
        @AuthenticationPrincipal User user) {

        List<GroupLoginUserParticipatedResponseDto> userGroup = groupService.findGroupListLoginUserParticipateForMemoryCreate(
            Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                MemoryAssembler.groupListForSaveMemoryResponse(userGroup)));
    }

    @GetMapping("/user/place/{placeId}")
    public ResponseEntity<ApiResponse> getUserMemory(
        @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
        @AuthenticationPrincipal User user, @PathVariable Long placeId,
        @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> userMemory = memoryReadService.findLoginUserWriteMemoryBelongToPlace(
            pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(userMemory);
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                new GetMemoryForPlaceResponseWrapper(memoryResponses.getContent().stream().count(),
                    memoryResponses)));
    }

    @GetMapping("/other/place/{placeId}")
    public ResponseEntity<ApiResponse> getOtherMemory(@AuthenticationPrincipal User user,
        @PathVariable Long placeId, @RequestParam(required = false) GroupType groupType,
        @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {

        Slice<MemoryResponseDto> otherMemory = memoryReadService.findOtherUserWriteMemoryBelongToPlace(
            pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(otherMemory);
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                new GetOtherMemoryForPlaceResponseWrapper(memoryResponses.stream().count(),
                    memoryResponses)));
    }

    @GetMapping("/group/place/{placeId}")
    public ResponseEntity<ApiResponse> getMyGroupMemory(@AuthenticationPrincipal User user,
        @PathVariable Long placeId, Pageable pageable,
        @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> results = memoryReadService.findMyGroupMemberWriteMemoryBelongToPlace(
            pageable, Long.parseLong(user.getUsername()), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(results);
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                new GetMyGroupMemoryForPlaceResponseWrapper(memoryResponses.stream().count(),
                    memoryResponses)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> save(@AuthenticationPrincipal User user,
        @RequestPart(name = "images", required = false) List<MultipartFile> images,
        @Valid @RequestPart(name = "memoryData") MemoryCreateRequest memoryCreateRequest) {

        memoryWriteService.createMemory(
            MemoryAssembler.createMemoryRequestDto(Long.parseLong(user.getUsername()), images,
                memoryCreateRequest));
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @PutMapping("/{memoryId}")
    public ResponseEntity<ApiResponse> updateMemory(@AuthenticationPrincipal User user,
        @PathVariable Long memoryId,
        @RequestPart(name = "images", required = false) List<MultipartFile> images,
        @RequestPart(name = "memoryData") MemoryUpdateRequest memoryUpdateRequest) {

        memoryWriteService.updateMemory(
            MemoryAssembler.updateMemoryRequestDto(Long.parseLong(user.getUsername()), memoryId,
                memoryUpdateRequest, images));
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @GetMapping("/{memoryId}/update")
    public ResponseEntity<ApiResponse> getFormForUpdateMemory(@AuthenticationPrincipal User user,
        @PathVariable Long memoryId) {

        MemoryUpdateFormResponseDto formForUpdateMemory = memoryReadService.findMemoryUpdateFormData(
            Long.parseLong(user.getUsername()), memoryId);
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                MemoryAssembler.memoryUpdateFormResponse(formForUpdateMemory)));
    }

    @DeleteMapping("/{memoryId}")
    public ResponseEntity<ApiResponse> deleteMemory(@PathVariable Long memoryId) {

        memoryWriteService.removeMemory(memoryId);
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @GetMapping("/{memoryId}")
    public ResponseEntity<ApiResponse> findMemory(@AuthenticationPrincipal User user,
        @PathVariable Long memoryId) {

        MemoryResponseDto memoryByMemoryId = memoryReadService.findMemoryByMemoryId(
            Long.parseLong(user.getUsername()), memoryId);
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                MemoryResponse.of(memoryByMemoryId)));
    }
}
