package cmc.mellyserver.user.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.presentation.dto.GetUserMemoryCond;
import cmc.mellyserver.scrap.application.ScrapService;
import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.scrap.presentation.dto.ScrapResponseWrapper;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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


    /**
     * 메모리도 날짜 순으로 정렬할 수 있도록 날짜 함께 보내주기
     */
    @Operation(summary = "유저가 작성한 메모리 조회")
    @GetMapping("/memory")
    public ResponseEntity<CommonResponse> getUserMemory(@AuthenticationPrincipal User user,
                                                        @RequestParam(name = "lastId", required = false) Long lastId,
                                                        Pageable pageable,
                                                        GetUserMemoryCond getUserMemoryCond) {
        Slice<Memory> userMemory = userService.getUserMemory(lastId, pageable, user.getUsername(), getUserMemoryCond);
        return ResponseEntity.ok(new CommonResponse(200,
                "유저가 작성한 메모리 조회", new GetUserMemoryResponseWrapper(UserAssembler.getUserMemoryResponses(userMemory))
        ));
    }


    /**
     * 스크랩 장소 보여줄 시 날짜 순으로 최신꺼부터 정렬 -> 날짜 함께 보내줘야함
     */
    @Operation(summary = "유저가 스크랩한 장소 조회")
    @GetMapping("/scrap")
    public ResponseEntity<CommonResponse> getUserScrap(@AuthenticationPrincipal User user, @RequestParam(name = "lastId", required = false) Long lastId, Pageable pageable) {
        Slice<ScrapedPlaceResponseDto> scrapedPlace = scrapService.getScrapedPlace(lastId, pageable, user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200, "유저가 스크랩한 장소 목록", new ScrapResponseWrapper(scrapedPlace)));
    }


    /**
     * 그룹 정보 보여줄때도 날짜 함께 보내주기
     */
    @Operation(summary = "유저가 속해있는 그룹 조회")
    @GetMapping("/group")
    public ResponseEntity<CommonResponse> getUserGroup(@AuthenticationPrincipal User user) {
        List<UserGroup> userGroup = userService.getUserGroup(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,
                "유저가 속해있는 그룹 조회", new GetUserGroupResponseWrapper(UserAssembler.getUserGroupResponses(userGroup))
        ));
    }


    @Operation(summary = "유저가 저장한 이미지 용량 조회")
    @GetMapping("/volume")
    public ResponseEntity<CommonResponse> getUserImageVolume(@AuthenticationPrincipal User user) {
        double volume = userService.checkUserImageVolume(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200, "유저가 저장한 사진 총 용량", new UserImageVolumeWrapper(volume)));
    }


    @Operation(summary = "초대링크를 받은 후 그룹에 참여")
    @PostMapping("/group")
    public ResponseEntity<CommonResponse> participateToGroup(@AuthenticationPrincipal User user, @RequestBody ParticipateGroupRequest participateGroupRequest) {
        userService.participateToGroup(user.getUsername(), participateGroupRequest.getGroupId());
        return ResponseEntity.ok(new CommonResponse(200, "그룹에 추가 완료"));
    }


    @Operation(summary = "프로필 정보 수정 기능")
    @PutMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@AuthenticationPrincipal User user, ProfileUpdateRequest profileUpdateRequest) {
        userService.updateProfile(user.getUsername(), profileUpdateRequest);
        return ResponseEntity.ok(new CommonResponse(200, "프로필 수정 완료"));
    }


}
