package cmc.mellyserver.controller.memory;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

import cmc.mellyserver.auth.controller.dto.common.CurrentUser;
import cmc.mellyserver.auth.controller.dto.common.LoginUser;
import cmc.mellyserver.controller.memory.dto.MemoryAssembler;
import cmc.mellyserver.controller.memory.dto.request.MemoryCreateRequest;
import cmc.mellyserver.controller.memory.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.controller.memory.dto.response.MemoryDetailResponse;
import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.domain.group.GroupService;
import cmc.mellyserver.domain.group.dto.response.UserJoinedGroupsResponse;
import cmc.mellyserver.domain.memory.MemoryService;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.memory.query.dto.MemoryDetailResponseDto;
import cmc.mellyserver.support.response.ApiResponse;
import cmc.mellyserver.support.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryService memoryService;

    private final GroupService groupService;

    @GetMapping("/group")
    public ResponseEntity<ApiResponse<UserJoinedGroupsResponse>> getGroupListForSaveMemory(
        @CurrentUser LoginUser loginUser, @RequestParam(name = "lastId", required = false) Long lastId,
        @PageableDefault(size = 10) Pageable pageable) {

        UserJoinedGroupsResponse groupListLoginUserParticiated = groupService
            .findUserParticipatedGroups(loginUser.getId(), lastId, pageable);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, groupListLoginUserParticiated);
    }

    @GetMapping("/user/places/{placeId}")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getUserMemory(@CurrentUser LoginUser loginUser,
        @RequestParam(name = "lastId", required = false) Long lastId, @PageableDefault(size = 10) Pageable pageable,
        @PathVariable Long placeId, @RequestParam(required = false) GroupType groupType) {

        MemoryListResponse loginUserWriteMemoryBelongToPlace = memoryService
            .getUserMemoriesInPlace(lastId, pageable, loginUser.getId(), placeId, groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, loginUserWriteMemoryBelongToPlace);
    }

    @GetMapping("/other/place/{placeId}")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getOtherMemory(@CurrentUser LoginUser loginUser,
        @RequestParam(name = "lastId", required = false) Long lastId, @PageableDefault(size = 10) Pageable pageable,
        @PathVariable Long placeId, @RequestParam(required = false) GroupType groupType) {

        MemoryListResponse otherUserWriteMemoryBelongToPlace = memoryService
            .getOtherMemoriesInPlace(lastId, pageable, loginUser.getId(), placeId, groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, otherUserWriteMemoryBelongToPlace);
    }

    @GetMapping("/groups/places/{placeId}")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getMyGroupMemory(@CurrentUser LoginUser loginUser,
        @RequestParam(name = "lastId", required = false) Long lastId, @PathVariable Long placeId,
        @PageableDefault(size = 10) Pageable pageable, @RequestParam(required = false) GroupType groupType) {

        MemoryListResponse myGroupMemberWriteMemoryBelongToPlace = memoryService
            .getGroupMemoriesInPlace(lastId, pageable, loginUser.getId(), placeId, groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, myGroupMemberWriteMemoryBelongToPlace);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> save(@CurrentUser LoginUser loginUser,
        @RequestPart(name = "memoryImages", required = false) List<MultipartFile> images,
        @Valid @RequestPart(name = "memoryData") MemoryCreateRequest memoryCreateRequest) {

        memoryService
            .createMemory(MemoryAssembler.createMemoryRequestDto(loginUser.getId(), images, memoryCreateRequest));
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PutMapping("/{memoryId}")
    public ResponseEntity<ApiResponse<Void>> updateMemory(@CurrentUser LoginUser loginUser, @PathVariable Long memoryId,
        @RequestPart(name = "memoryImages", required = false) List<MultipartFile> images,
        @RequestPart(name = "memoryData") MemoryUpdateRequest memoryUpdateRequest) {

        memoryService.updateMemory(
            MemoryAssembler.updateMemoryRequestDto(loginUser.getId(), memoryId, memoryUpdateRequest, images));
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    @DeleteMapping("/{memoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteMemory(@PathVariable Long memoryId) {

        memoryService.removeMemory(memoryId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @GetMapping("/{memoryId}")
    public ResponseEntity<ApiResponse<MemoryDetailResponse>> getMemory(@PathVariable Long memoryId) {

        MemoryDetailResponseDto memoryByMemoryId = memoryService.getMemory(memoryId);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, MemoryDetailResponse.of(memoryByMemoryId));
    }

}
