package cmc.mellyserver.mellyapi.group.presentation;

import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.common.code.SuccessCode;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.group.presentation.dto.GroupAssembler;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.GroupLoginUserParticipatedResponse;
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
    public ResponseEntity<ApiResponse<GroupLoginUserParticipatedResponse>> getGroupDetail(@PathVariable Long groupId) {

        GroupDetailResponseDto groupDetail = groupService.getGroupDetail(groupId);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, GroupAssembler.getUserGroupResponse(groupDetail));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addGroup(@CurrentUser LoginUser loginUser, @Valid @RequestBody GroupCreateRequest groupCreateRequest) {

        groupService.saveGroup(GroupAssembler.createGroupRequestDto(loginUser.getId(), groupCreateRequest));
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<ApiResponse<Void>> updateGroup(@PathVariable Long groupId, @CurrentUser LoginUser loginUser, @Valid @RequestBody GroupUpdateRequest groupUpdateRequest) {

        groupService.updateGroup(GroupAssembler.updateGroupRequestDto(groupId, groupUpdateRequest));
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    @PostMapping("/{groupId}/participate")
    public ResponseEntity<ApiResponse<Void>> participateToGroup(@CurrentUser LoginUser loginUser, @PathVariable(name = "groupId") Long groupId) {

        groupService.participateToGroup(loginUser.getId(), groupId);
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@CurrentUser LoginUser loginUser, @PathVariable(name = "groupId") Long groupId) {

        groupService.removeGroup(loginUser.getId(), groupId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @DeleteMapping("/{groupId}/exit")
    public ResponseEntity<ApiResponse<Void>> exitGroup(@CurrentUser LoginUser loginUser, @PathVariable Long groupId) {

        groupService.exitGroup(loginUser.getId(), groupId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }
}
