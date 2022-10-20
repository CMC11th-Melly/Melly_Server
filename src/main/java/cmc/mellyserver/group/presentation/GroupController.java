package cmc.mellyserver.group.presentation;

import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.presentation.dto.GroupCreateRequest;
import cmc.mellyserver.group.presentation.dto.GroupCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/group")
    private ResponseEntity<GroupCreateResponse> addGroup(@AuthenticationPrincipal User user,GroupCreateRequest groupCreateRequest)
    {
        UserGroup userGroup = groupService.saveGroup(user.getUsername(), groupCreateRequest);
        return ResponseEntity.ok(new GroupCreateResponse(userGroup.getId(),userGroup.getGroupName()));
    }

}
