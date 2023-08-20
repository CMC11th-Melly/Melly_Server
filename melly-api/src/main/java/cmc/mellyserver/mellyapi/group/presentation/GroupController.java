package cmc.mellyserver.mellyapi.group.presentation;

import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.common.code.SuccessCode;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.group.presentation.dto.GroupAssembler;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.mellycore.group.application.GroupService;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{groupId}")
    public ResponseEntity<ApiResponse> getGroupDetail(@PathVariable Long groupId) {

        GroupDetailResponseDto groupDetail = groupService.getGroupDetail(groupId);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, GroupAssembler.getUserGroupResponse(groupDetail));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addGroup(@CurrentUser LoginUser loginUser, @Valid @RequestBody GroupCreateRequest groupCreateRequest) {

        Long groupId = groupService.saveGroup(GroupAssembler.createGroupRequestDto(loginUser.getId(), groupCreateRequest));
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<ApiResponse> updateGroup(@PathVariable Long groupId, @CurrentUser LoginUser loginUser, @Valid @RequestBody GroupUpdateRequest groupUpdateRequest) {

        groupService.updateGroup(loginUser.getId(), GroupAssembler.updateGroupRequestDto(groupId, groupUpdateRequest));
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    @PostMapping("/{groupId}/participate")
    public ResponseEntity<ApiResponse> participateToGroup(@CurrentUser LoginUser loginUser, @PathVariable(name = "groupId") Long groupId) {

        groupService.participateToGroup(loginUser.getId(), groupId);
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ApiResponse> deleteGroup(@CurrentUser LoginUser loginUser, @PathVariable(name = "groupId") Long groupId) {

        groupService.removeGroup(loginUser.getId(), groupId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @DeleteMapping("/{groupId}/exit")
    public ResponseEntity<ApiResponse> exitGroup(@CurrentUser LoginUser loginUser, @PathVariable Long groupId) {

        groupService.exitGroup(loginUser.getId(), groupId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }
}
