package cmc.mellyserver.user.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.scrap.application.ScrapService;
import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.scrap.presentation.dto.ScrapResponseWrapper;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.presentation.dto.GetUserGroupResponseWrapper;
import cmc.mellyserver.user.presentation.dto.GetUserMemoryResponseWrapper;
import cmc.mellyserver.user.presentation.dto.ParticipateGroupRequest;
import cmc.mellyserver.user.presentation.dto.UserAssembler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final ScrapService scrapService;
    private final UserService userService;

    @Operation(summary = "유저가 스크랩한 장소 조회")
    @GetMapping("/scrap")
    public ResponseEntity<CommonResponse> getUserScrap(@AuthenticationPrincipal User user)
    {
        List<ScrapedPlaceResponseDto> scrapedPlace = scrapService.getScrapedPlace(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"유저가 스크랩한 장소 목록",new ScrapResponseWrapper(scrapedPlace)));
    }

    @Operation(summary = "유저가 작성한 메모리 조회")
    @GetMapping("/memory")
    public ResponseEntity<CommonResponse> getUserMemory(@AuthenticationPrincipal User user)
    {
        List<Memory> userMemory = userService.getUserMemory(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,
                "유저가 작성한 메모리 조회",new GetUserMemoryResponseWrapper(UserAssembler.getUserMemoryResponses(userMemory))
        ));
    }

    @Operation(summary = "유저가 저장한 이미지 용량 조회")
    @GetMapping("/volume")
    public void getUserImageVolume(@AuthenticationPrincipal User user)
    {

    }

    @Operation(summary = "유저가 속해있는 그룹 조회")
    @GetMapping("/group")
    public ResponseEntity<CommonResponse> getUserGroup(@AuthenticationPrincipal User user)
    {
        List<UserGroup> userGroup = userService.getUserGroup(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,
                "유저가 작성한 메모리 조회",new GetUserGroupResponseWrapper(UserAssembler.getUserGroupResponses(userGroup))
        ));
    }

    @Operation(summary = "초대링크를 받은 후 그룹에 참여")
    @PostMapping("/group")
    public ResponseEntity<CommonResponse> participateToGroup(@AuthenticationPrincipal User user, @RequestBody ParticipateGroupRequest participateGroupRequest)
    {
        userService.participateToGroup(user.getUsername(),participateGroupRequest.getGroupId());
        return ResponseEntity.ok(new CommonResponse(200,"그룹에 추가 완료"));
    }



}
