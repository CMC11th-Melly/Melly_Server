package cmc.mellyserver.mellyapi.group.presentation;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.mellyapi.common.response.CommonResponse;
import cmc.mellyserver.mellyapi.group.application.GroupService;
import cmc.mellyserver.mellyapi.group.presentation.dto.GroupAssembler;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.GroupLoginUserParticipatedResponse;
import cmc.mellyserver.mellycore.common.constants.MessageConstant;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {

	private final GroupService groupService;

	@GetMapping("/{groupId}")
	private ResponseEntity<CommonResponse> getGroupInfo(@PathVariable Long groupId) {
		UserGroup group = groupService.findGroupById(groupId);
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
			GroupAssembler.getUserGroupResponse(group)));
	}

	@PostMapping
	private ResponseEntity<CommonResponse> addGroup(@AuthenticationPrincipal User user,
		@Valid @RequestBody GroupCreateRequest groupCreateRequest) {
		UserGroup userGroup = groupService.saveGroup(
			GroupAssembler.createGroupRequestDto(Long.parseLong(user.getUsername()), groupCreateRequest));
		GroupLoginUserParticipatedResponse userGroupResponse = GroupAssembler.getUserGroupResponse(userGroup);
		return ResponseEntity.ok(
			new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, userGroupResponse));
	}

	@PutMapping("/{groupId}")
	private ResponseEntity<CommonResponse> updateGroup(@PathVariable Long groupId,
		@Valid @RequestBody GroupUpdateRequest groupUpdateRequest) {
		groupService.updateGroup(GroupAssembler.updateGroupRequestDto(groupId, groupUpdateRequest));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@DeleteMapping("/{groupId}")
	private ResponseEntity<CommonResponse> deleteGroup(@AuthenticationPrincipal User user, @PathVariable Long groupId) {
		groupService.removeGroup(Long.parseLong(user.getUsername()), groupId);
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}
}
