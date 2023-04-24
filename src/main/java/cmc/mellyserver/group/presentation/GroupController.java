package cmc.mellyserver.group.presentation;

import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.group.UserGroup;
import cmc.mellyserver.group.presentation.dto.GroupAssembler;
import cmc.mellyserver.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.user.presentation.dto.response.GetUserGroupResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;


    // TODO : 수정 완료 (2023.04.16)
    @GetMapping("/group/{groupId}")
    private ResponseEntity<CommonResponse> getGroupInfo(@PathVariable Long groupId)
    {
        UserGroup group = groupService.getGroupById(groupId);
        return ResponseEntity.ok(new CommonResponse(200,"성공",GroupAssembler.getUserGroupResponse(group)));
    }


    // TODO : 수정 완료 (2023.04.16)
    @Operation(summary = "그룹 추가")
    @PostMapping("/group")
    private ResponseEntity<CommonResponse> addGroup(@AuthenticationPrincipal User user,@Valid @RequestBody GroupCreateRequest groupCreateRequest)
    {
        UserGroup userGroup = groupService.saveGroup(Long.parseLong(user.getUsername()), groupCreateRequest);
        GetUserGroupResponse userGroupResponse = GroupAssembler.getUserGroupResponse(userGroup);
        return ResponseEntity.ok(new CommonResponse(200,"그룹 추가 완료",new CommonDetailResponse<>(userGroupResponse)));
    }


    // TODO : 수정 완료 (2023.04.16)
    @Operation(summary = "그룹 편집")
    @PutMapping("/group/{groupId}")
    private ResponseEntity<CommonResponse> updateGroup(@PathVariable Long groupId, @Valid @RequestBody GroupUpdateRequest groupUpdateRequest)
    {
        groupService.updateGroup(groupId,groupUpdateRequest);
        return ResponseEntity.ok(new CommonResponse(200,"그룹 수정 완료"));
    }


    // TODO : 수정 완료 (2023.04.16)
    @Operation(summary = "그룹 삭제")
    @DeleteMapping("/group/{groupId}")
    private ResponseEntity<CommonResponse> deleteGroup(@AuthenticationPrincipal User user,@PathVariable Long groupId)
    {
        String message = groupService.deleteGroup(Long.parseLong(user.getUsername()), groupId);
        return ResponseEntity.ok(new CommonResponse(200,message));
    }


}
