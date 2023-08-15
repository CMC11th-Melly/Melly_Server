package cmc.mellyserver.mellyapi.memory.presentation;

import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.common.code.SuccessCode;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.MemoryAssembler;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper.GetMemoryForPlaceResponseWrapper;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper.GetMyGroupMemoryForPlaceResponseWrapper;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper.GetOtherMemoryForPlaceResponseWrapper;
import cmc.mellyserver.mellyapi.memory.presentation.dto.request.MemoryCreateRequest;
import cmc.mellyserver.mellyapi.memory.presentation.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.mellyapi.memory.presentation.dto.response.MemoryDetailResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.response.MemoryResponse;
import cmc.mellyserver.mellycore.group.application.GroupService;
import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.application.MemoryReadService;
import cmc.mellyserver.mellycore.memory.application.MemoryWriteService;
import cmc.mellyserver.mellycore.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryDetailResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryReadService memoryReadService;

    private final MemoryWriteService memoryWriteService;

    private final GroupService groupService;

    @GetMapping("/group")
    public ResponseEntity<ApiResponse> getGroupListForSaveMemory(@CurrentUser LoginUser loginUser, @RequestParam(name = "lastId", required = false) Long lastId, @PageableDefault(size = 10) Pageable pageable) {

        Slice<GroupLoginUserParticipatedResponseDto> userGroup = groupService.findGroupListLoginUserParticipateForMemoryCreate(loginUser.getId(), lastId, pageable);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, MemoryAssembler.groupListForSaveMemoryResponse(userGroup));
    }


    @GetMapping("/user/place/{placeId}")
    public ResponseEntity<ApiResponse> getUserMemory(@CurrentUser LoginUser loginUser,
                                                     @RequestParam(name = "lastId", required = false) Long lastId,
                                                     @PageableDefault(size = 10) Pageable pageable,
                                                     @PathVariable Long placeId,
                                                     @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> userMemory = memoryReadService.findLoginUserWriteMemoryBelongToPlace(lastId, pageable, loginUser.getId(), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(userMemory);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, new GetMemoryForPlaceResponseWrapper(memoryResponses.getContent().stream().count(), memoryResponses));
    }

    @GetMapping("/other/place/{placeId}")
    public ResponseEntity<ApiResponse> getOtherMemory(@CurrentUser LoginUser loginUser,
                                                      @RequestParam(name = "lastId", required = false) Long lastId,
                                                      @PageableDefault(size = 10) Pageable pageable,
                                                      @PathVariable Long placeId,
                                                      @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> otherMemory = memoryReadService.findOtherUserWriteMemoryBelongToPlace(lastId, pageable, loginUser.getId(), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(otherMemory);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, new GetOtherMemoryForPlaceResponseWrapper(memoryResponses.stream().count(), memoryResponses));
    }

    @GetMapping("/group/place/{placeId}")
    public ResponseEntity<ApiResponse> getMyGroupMemory(@CurrentUser LoginUser loginUser,
                                                        @RequestParam(name = "lastId", required = false) Long lastId,
                                                        @PathVariable Long placeId,
                                                        @PageableDefault(size = 10) Pageable pageable,
                                                        @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> results = memoryReadService.findMyGroupMemberWriteMemoryBelongToPlace(lastId, pageable, loginUser.getId(), placeId, groupType);
        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(results);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, new GetMyGroupMemoryForPlaceResponseWrapper(memoryResponses.stream().count(), memoryResponses));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> save(@CurrentUser LoginUser loginUser,
                                            @RequestPart(name = "memoryImages", required = false) List<MultipartFile> images,
                                            @Valid @RequestPart(name = "memoryData") MemoryCreateRequest memoryCreateRequest) {

        Long memoryId = memoryWriteService.createMemory(MemoryAssembler.createMemoryRequestDto(loginUser.getId(), images, memoryCreateRequest));
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PutMapping("/{memoryId}")
    public ResponseEntity<ApiResponse> updateMemory(@CurrentUser LoginUser loginUser,
                                                    @PathVariable Long memoryId,
                                                    @RequestPart(name = "memoryImages", required = false) List<MultipartFile> images,
                                                    @RequestPart(name = "memoryData") MemoryUpdateRequest memoryUpdateRequest) {

        memoryWriteService.updateMemory(MemoryAssembler.updateMemoryRequestDto(loginUser.getId(), memoryId, memoryUpdateRequest, images));
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    @GetMapping("/{memoryId}/update")
    public ResponseEntity<ApiResponse> getFormForUpdateMemory(@CurrentUser LoginUser loginUser, @PathVariable Long memoryId) {

        MemoryUpdateFormResponseDto formForUpdateMemory = memoryReadService.findMemoryUpdateFormData(loginUser.getId(), memoryId);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, MemoryAssembler.memoryUpdateFormResponse(formForUpdateMemory));
    }

    @DeleteMapping("/{memoryId}")
    public ResponseEntity<ApiResponse> deleteMemory(@PathVariable Long memoryId) {

        memoryWriteService.removeMemory(memoryId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }


    @GetMapping("/{memoryId}")
    public ResponseEntity<ApiResponse> findMemoryDetail(@PathVariable Long memoryId) {

        MemoryDetailResponseDto memoryByMemoryId = memoryReadService.findMemoryDetail(memoryId);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, MemoryDetailResponse.of(memoryByMemoryId));
    }
}
