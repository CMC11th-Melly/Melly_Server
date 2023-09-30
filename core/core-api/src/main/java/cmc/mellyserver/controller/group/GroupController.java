package cmc.mellyserver.controller.group;

import cmc.mellyserver.controller.group.dto.GroupAssembler;
import cmc.mellyserver.controller.group.dto.request.GroupCreateRequest;
import cmc.mellyserver.controller.group.dto.request.GroupUpdateRequest;
import cmc.mellyserver.domain.group.GroupService;
import cmc.mellyserver.domain.group.query.dto.GroupDetailResponseDto;
import cmc.mellyserver.common.code.SuccessCode;
import cmc.mellyserver.controller.auth.dto.common.CurrentUser;
import cmc.mellyserver.controller.auth.dto.common.LoginUser;
import cmc.mellyserver.controller.user.dto.response.GroupLoginUserParticipatedResponse;
import cmc.mellyserver.support.response.ApiResponse;
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
