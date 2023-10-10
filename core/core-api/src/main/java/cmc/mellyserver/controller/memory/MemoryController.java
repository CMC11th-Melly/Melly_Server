package cmc.mellyserver.controller.memory;

import cmc.mellyserver.auth.controller.dto.common.CurrentUser;
import cmc.mellyserver.auth.controller.dto.common.LoginUser;
import cmc.mellyserver.common.code.SuccessCode;
import cmc.mellyserver.controller.memory.dto.MemoryAssembler;
import cmc.mellyserver.controller.memory.dto.request.MemoryCreateRequest;
import cmc.mellyserver.controller.memory.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.controller.memory.dto.response.MemoryDetailResponse;
import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.domain.group.GroupService;
import cmc.mellyserver.domain.group.dto.response.GroupListLoginUserParticipatedResponse;
import cmc.mellyserver.domain.memory.MemoryReadService;
import cmc.mellyserver.domain.memory.MemoryWriteService;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.memory.query.dto.MemoryDetailResponseDto;
import cmc.mellyserver.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryReadService memoryReadService;

    private final MemoryWriteService memoryWriteService;

    private final GroupService groupService;

    @GetMapping("/group")
    public ResponseEntity<ApiResponse<GroupListLoginUserParticipatedResponse>> getGroupListForSaveMemory(@CurrentUser LoginUser loginUser, @RequestParam(name = "lastId", required = false) Long lastId, @PageableDefault(size = 10) Pageable pageable) {

        GroupListLoginUserParticipatedResponse groupListLoginUserParticiated = groupService.findGroupListLoginUserParticiated(loginUser.getId(), lastId, pageable);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, groupListLoginUserParticiated);
    }


    @GetMapping("/user/place/{placeId}")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getUserMemory(@CurrentUser LoginUser loginUser,
                                                                         @RequestParam(name = "lastId", required = false) Long lastId,
                                                                         @PageableDefault(size = 10) Pageable pageable,
                                                                         @PathVariable Long placeId,
                                                                         @RequestParam(required = false) GroupType groupType) {

        MemoryListResponse loginUserWriteMemoryBelongToPlace = memoryReadService.findLoginUserWriteMemoryBelongToPlace(lastId, pageable, loginUser.getId(), placeId, groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, loginUserWriteMemoryBelongToPlace);
    }

    @GetMapping("/other/place/{placeId}")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getOtherMemory(@CurrentUser LoginUser loginUser,
                                                                          @RequestParam(name = "lastId", required = false) Long lastId,
                                                                          @PageableDefault(size = 10) Pageable pageable,
                                                                          @PathVariable Long placeId,
                                                                          @RequestParam(required = false) GroupType groupType) {

        MemoryListResponse otherUserWriteMemoryBelongToPlace = memoryReadService.findOtherUserWriteMemoryBelongToPlace(lastId, pageable, loginUser.getId(), placeId, groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, otherUserWriteMemoryBelongToPlace);
    }

    @GetMapping("/group/place/{placeId}")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getMyGroupMemory(@CurrentUser LoginUser loginUser,
                                                                            @RequestParam(name = "lastId", required = false) Long lastId,
                                                                            @PathVariable Long placeId,
                                                                            @PageableDefault(size = 10) Pageable pageable,
                                                                            @RequestParam(required = false) GroupType groupType) {

        MemoryListResponse myGroupMemberWriteMemoryBelongToPlace = memoryReadService.findMyGroupMemberWriteMemoryBelongToPlace(lastId, pageable, loginUser.getId(), placeId, groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, myGroupMemberWriteMemoryBelongToPlace);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> save(@CurrentUser LoginUser loginUser,
                                                  @RequestPart(name = "memoryImages", required = false) List<MultipartFile> images,
                                                  @Valid @RequestPart(name = "memoryData") MemoryCreateRequest memoryCreateRequest) {

        memoryWriteService.createMemory(MemoryAssembler.createMemoryRequestDto(loginUser.getId(), images, memoryCreateRequest));
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PutMapping("/{memoryId}")
    public ResponseEntity<ApiResponse<Void>> updateMemory(@CurrentUser LoginUser loginUser,
                                                          @PathVariable Long memoryId,
                                                          @RequestPart(name = "memoryImages", required = false) List<MultipartFile> images,
                                                          @RequestPart(name = "memoryData") MemoryUpdateRequest memoryUpdateRequest) {

        memoryWriteService.updateMemory(MemoryAssembler.updateMemoryRequestDto(loginUser.getId(), memoryId, memoryUpdateRequest, images));
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }


    @DeleteMapping("/{memoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteMemory(@PathVariable Long memoryId) {

        memoryWriteService.removeMemory(memoryId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }


    @GetMapping("/{memoryId}")
    public ResponseEntity<ApiResponse<MemoryDetailResponse>> findMemoryDetail(@PathVariable Long memoryId) {

        MemoryDetailResponseDto memoryByMemoryId = memoryReadService.findMemoryDetail(memoryId);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, MemoryDetailResponse.of(memoryByMemoryId));
    }
}
