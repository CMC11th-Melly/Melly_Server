package cmc.mellyserver.mellyapi.group.presentation;

import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
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

import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.CREATED;
import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.NO_CONTENT;
import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.OK;
import static cmc.mellyserver.mellyapi.common.response.ApiResponse.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{groupId}")
    public ResponseEntity<ApiResponse> getGroupDetail(@PathVariable Long groupId) {

        GroupDetailResponseDto groupDetail = groupService.getGroupDetail(groupId);
        return OK(GroupAssembler.getUserGroupResponse(groupDetail));
    }

    @PostMapping
    public ResponseEntity<Void> addGroup(@CurrentUser LoginUser loginUser, @Valid @RequestBody GroupCreateRequest groupCreateRequest) {

        groupService.saveGroup(GroupAssembler.createGroupRequestDto(loginUser.getId(), groupCreateRequest));
        return CREATED;
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Void> updateGroup(@PathVariable Long groupId, @CurrentUser LoginUser loginUser, @Valid @RequestBody GroupUpdateRequest groupUpdateRequest) {

        groupService.updateGroup(loginUser.getId(), GroupAssembler.updateGroupRequestDto(groupId, groupUpdateRequest));
        return NO_CONTENT;
    }

    @PostMapping("/{groupId}/participate")
    public ResponseEntity<Void> participateToGroup(@CurrentUser LoginUser loginUser, @PathVariable(name = "groupId") Long groupId) {

        groupService.participateToGroup(loginUser.getId(), groupId);
        return CREATED;
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ApiResponse> deleteGroup(@CurrentUser LoginUser loginUser, @PathVariable(name = "groupId") Long groupId) {

        groupService.removeGroup(loginUser.getId(), groupId);
        return OK;
    }

    @DeleteMapping("/{groupId}/exit")
    public ResponseEntity<ApiResponse> exitGroup(@CurrentUser LoginUser loginUser, @PathVariable Long groupId) {
        groupService.exitGroup(loginUser.getId(), groupId);
        return OK;
    }


}
