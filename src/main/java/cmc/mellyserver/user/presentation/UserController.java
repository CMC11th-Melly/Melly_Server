package cmc.mellyserver.user.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memoryScrap.application.MemoryScrapService;
import cmc.mellyserver.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import cmc.mellyserver.memoryScrap.presentation.dto.MemoryScrapResponseWrapper;
import cmc.mellyserver.placeScrap.application.PlaceScrapService;
import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.placeScrap.presentation.dto.ScrapResponseWrapper;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.application.dto.GroupMemory;
import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponse;
import cmc.mellyserver.user.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final PlaceScrapService placeScrapService;
    private final MemoryScrapService memoryScrapService;
    private final UserService userService;


    /**
     * 마이페이지 - My 메모리
     */
    @Operation(summary = "유저가 작성한 메모리 조회")
    @GetMapping("/memory")
    public ResponseEntity<CommonResponse> getUserMemory(@AuthenticationPrincipal User user,
                                                        @PageableDefault(sort = "visitedDate",
                                                                direction = Sort.Direction.ASC,size = 10) Pageable pageable,
                                                        @RequestParam(required = false) GroupType groupType) {
        Slice<Memory> userMemory = userService.getUserMemory(pageable, user.getUsername(), groupType);
        return ResponseEntity.ok(new CommonResponse(200,
                "My 메모리 조회", new GetUserMemoryResponseWrapper(UserAssembler.getUserMemoryResponses(userMemory))
        ));
    }

    /**
     * 마이페이지 - My 그룹
     */
    @Operation(summary = "유저가 속해있는 그룹 조회")
    @GetMapping("/group")
    public ResponseEntity<CommonResponse> getUserGroup(@AuthenticationPrincipal User user) {
        List<UserGroup> userGroup = userService.getUserGroup(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,
                "My 그룹 조회", new GetUserGroupResponseWrapper(UserAssembler.getUserGroupResponses(userGroup,user.getUsername()))
        ));
    }

    /**
     * 마이페이지 - My 그룹 - 내 그룹의 메모리 모두 조회
     */
    @Operation(summary = "유저가 속해있는 그룹의 메모리 조회")
    @GetMapping("/group/{groupId}/memory")
    public ResponseEntity<CommonResponse> getMemoryBelongToMyGroup(Pageable pageable, @PathVariable Long groupId,@RequestParam(required = false) GroupType groupType)
    {
        Slice<GroupMemory> results = userService.getMemoryBelongToMyGroup(pageable, groupId,groupType);
        return ResponseEntity.ok(new CommonResponse(200,"내 그룹",results));
    }


    /**
     * 마이페이지 - My 장소 스크랩
     */
    @Operation(summary = "유저가 스크랩한 장소 조회")
    @GetMapping("/place/scrap")
    public ResponseEntity<CommonResponse> getPlaceUserScrap(@AuthenticationPrincipal User user,Pageable pageable) {
        Slice<ScrapedPlaceResponseDto> scrapedPlace = placeScrapService.getScrapedPlace(pageable, user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200, "My 스크랩 조회", new ScrapResponseWrapper(scrapedPlace)));
    }


    /**
     * 마이페이지 - My 메모리 스크랩 (데모데이 이후에 추가)
     */
    @Operation(summary = "유저가 스크랩한 메모리 조회")
    @GetMapping("/memory/scrap")
    public ResponseEntity<CommonResponse> getMemoryUserScrap(@AuthenticationPrincipal User user, Pageable pageable,@RequestParam(required = false) GroupType groupType) {

        System.out.println("pageable : " + pageable.getSort());
        Slice<ScrapedMemoryResponseDto> scrapedMemory = memoryScrapService.getScrapedMemory(pageable, user.getUsername(),groupType);
        return ResponseEntity.ok(new CommonResponse(200, "유저가 스크랩한 메모리 목록", new MemoryScrapResponseWrapper(scrapedMemory)));
    }


    /**
     * 유저 이미지 용량 조회
     */
    @Operation(summary = "유저가 저장한 이미지 용량 조회")
    @GetMapping("/volume")
    public ResponseEntity<CommonResponse> getUserImageVolume(@AuthenticationPrincipal User user) {
        int volume = userService.checkUserImageVolume(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200, "유저가 저장한 사진 총 용량", new UserImageVolumeWrapper(volume)));
    }


    @Operation(summary = "초대링크를 받은 후 그룹에 참여")
    @PostMapping("/group")
    public ResponseEntity<CommonResponse> participateToGroup(@AuthenticationPrincipal User user, @RequestBody ParticipateGroupRequest participateGroupRequest) {
        userService.participateToGroup(user.getUsername(), participateGroupRequest.getGroupId());
        return ResponseEntity.ok(new CommonResponse(200, "그룹에 추가 완료"));
    }

    @Operation(summary = "test")
    @GetMapping("/group/test")
    public void test(Pageable pageable, @AuthenticationPrincipal User user)
    {
        placeScrapService.getScrapedPlaceGroup(pageable,user.getUsername());
    }



    @Operation(summary = "프로필 수정 폼에 필요한 유저 정보 조회")
    @GetMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfileFormData(@AuthenticationPrincipal User user)
    {
        ProfileUpdateFormResponse profileDataForUpdate = userService.getProfileDataForUpdate(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"유저 프로필 수정을 위한 폼 정보",new ProfileUpdateFormResponseWrapper(profileDataForUpdate)));
    }

    @Operation(summary = "프로필 정보 수정 기능",description = "프로필 정보 수정할때 닉네임 중복 체크의 경우에는 기존 인증 로직에서 사용한 중복 체크 재활용하면 될 것 같아요~")
    @PutMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@AuthenticationPrincipal User user, ProfileUpdateRequest profileUpdateRequest) {
        userService.updateProfile(user.getUsername(), profileUpdateRequest);
        return ResponseEntity.ok(new CommonResponse(200, "프로필 수정 완료"));
    }


}
