package cmc.mellyserver.controller.group;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.auth.controller.dto.common.CurrentUser;
import cmc.mellyserver.auth.controller.dto.common.LoginUser;
import cmc.mellyserver.controller.group.dto.GroupAssembler;
import cmc.mellyserver.controller.group.dto.request.GroupCreateRequest;
import cmc.mellyserver.controller.group.dto.request.GroupUpdateRequest;
import cmc.mellyserver.controller.user.dto.response.GroupLoginUserParticipatedResponse;
import cmc.mellyserver.domain.group.GroupService;
import cmc.mellyserver.domain.group.query.dto.GroupDetailResponseDto;
import cmc.mellyserver.support.response.ApiResponse;
import cmc.mellyserver.support.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{groupId}")
    public ResponseEntity<ApiResponse<GroupLoginUserParticipatedResponse>> getGroupDetail(
        @CurrentUser LoginUser loginUser, @PathVariable Long groupId) {

        GroupDetailResponseDto groupDetail = groupService.getGroup(groupId, loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, GroupAssembler.getUserGroupResponse(groupDetail));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addGroup(@CurrentUser LoginUser loginUser,
        @Valid @RequestBody GroupCreateRequest groupCreateRequest) {

        groupService.saveGroup(GroupAssembler.createGroupRequestDto(loginUser.getId(), groupCreateRequest));
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<ApiResponse<Void>> updateGroup(@PathVariable Long groupId, @CurrentUser LoginUser loginUser,
        @Valid @RequestBody GroupUpdateRequest groupUpdateRequest) {

        groupService.updateGroup(GroupAssembler.updateGroupRequestDto(groupId, groupUpdateRequest));
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<ApiResponse<Void>> joinGroup(@CurrentUser LoginUser loginUser,
        @PathVariable(name = "groupId") Long groupId) {

        groupService.joinGroup(loginUser.getId(), groupId);
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@CurrentUser LoginUser loginUser,
        @PathVariable(name = "groupId") Long groupId) {

        groupService.removeGroup(groupId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @DeleteMapping("/{groupId}/exit")
    public ResponseEntity<ApiResponse<Void>> exitGroup(@CurrentUser LoginUser loginUser, @PathVariable Long groupId) {

        groupService.exitGroup(loginUser.getId(), groupId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

}
