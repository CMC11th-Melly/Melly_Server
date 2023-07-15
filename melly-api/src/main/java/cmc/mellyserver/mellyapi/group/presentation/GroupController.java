package cmc.mellyserver.mellyapi.group.presentation;

import cmc.mellyserver.mellyapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.group.presentation.dto.GroupAssembler;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.GroupLoginUserParticipatedResponse;
import cmc.mellyserver.mellycore.group.application.GroupService;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{groupId}")
    private ResponseEntity<ApiResponse> getGroupInfo(@PathVariable Long groupId) {

        UserGroup group = groupService.findGroupById(groupId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, GroupAssembler.getUserGroupResponse(group)));
    }

    @PostMapping
    private ResponseEntity<ApiResponse> addGroup(@AuthenticationPrincipal User user,
                                                 @Valid @RequestBody GroupCreateRequest groupCreateRequest) {

        UserGroup userGroup = groupService.saveGroup(GroupAssembler.createGroupRequestDto(Long.parseLong(user.getUsername()), groupCreateRequest));
        GroupLoginUserParticipatedResponse userGroupResponse = GroupAssembler.getUserGroupResponse(userGroup);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, userGroupResponse));
    }

    @PutMapping("/{groupId}")
    private ResponseEntity<ApiResponse> updateGroup(@PathVariable Long groupId, @AuthenticationPrincipal User user, @Valid @RequestBody GroupUpdateRequest groupUpdateRequest) {

        groupService.updateGroup(Long.parseLong(user.getUsername()), GroupAssembler.updateGroupRequestDto(groupId, groupUpdateRequest));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @DeleteMapping("/{groupId}")
    private ResponseEntity<ApiResponse> deleteGroup(@AuthenticationPrincipal User user, @PathVariable Long groupId) {

        groupService.removeGroup(Long.parseLong(user.getUsername()), groupId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }
}
