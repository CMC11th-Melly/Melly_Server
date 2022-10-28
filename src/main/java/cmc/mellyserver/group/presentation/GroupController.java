package cmc.mellyserver.group.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.presentation.dto.GroupCreateRequest;
import cmc.mellyserver.group.presentation.dto.GroupCreateResponse;
import cmc.mellyserver.group.presentation.dto.GroupUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 추가")
    @PostMapping("/group")
    private ResponseEntity<GroupCreateResponse> addGroup(@AuthenticationPrincipal User user,GroupCreateRequest groupCreateRequest)
    {
        UserGroup userGroup = groupService.saveGroup(user.getUsername(), groupCreateRequest);
        return ResponseEntity.ok(new GroupCreateResponse(userGroup.getId(),userGroup.getGroupName()));
    }

    @Operation(summary = "그룹 편집")
    @PutMapping("/group/{groupId}")
    private ResponseEntity<CommonResponse> updateGroup(
                                                       @PathVariable Long groupId,
                                                       @RequestBody GroupUpdateRequest groupUpdateRequest)
    {
        groupService.updateGroup(groupId,groupUpdateRequest);
        return ResponseEntity.ok(new CommonResponse(200,"그룹 수정 완료"));
    }

    @Operation(summary = "그룹 삭제")
    @DeleteMapping("/group/{groupId}")
    private ResponseEntity<CommonResponse> deleteGroup(@PathVariable Long groupId)
    {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok(new CommonResponse(200,"그룹 삭제 완료"));
    }


}
