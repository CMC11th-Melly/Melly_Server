package cmc.mellyserver.mellyapi.group.presentation;

import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.group.presentation.dto.GroupAssembler;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.mellycore.group.application.GroupService;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    private ResponseEntity<ApiResponse> getGroupInfo(@PathVariable Long groupId) {

        UserGroup group = groupService.findGroupById(groupId);
        return OK(GroupAssembler.getUserGroupResponse(group));
    }

    @PostMapping
    private ResponseEntity<Void> addGroup(@AuthenticationPrincipal User user, @Valid @RequestBody GroupCreateRequest groupCreateRequest) {

        groupService.saveGroup(GroupAssembler.createGroupRequestDto(Long.parseLong(user.getUsername()), groupCreateRequest));
        return CREATED;
    }

    @PutMapping("/{groupId}")
    private ResponseEntity<Void> updateGroup(@PathVariable Long groupId, @AuthenticationPrincipal User user, @Valid @RequestBody GroupUpdateRequest groupUpdateRequest) {

        groupService.updateGroup(Long.parseLong(user.getUsername()), GroupAssembler.updateGroupRequestDto(groupId, groupUpdateRequest));
        return NO_CONTENT;
    }

    @DeleteMapping("/{groupId}")
    private ResponseEntity<ApiResponse> deleteGroup(@AuthenticationPrincipal User user, @PathVariable Long groupId) {

        groupService.removeGroup(Long.parseLong(user.getUsername()), groupId);
        return OK;
    }
}
