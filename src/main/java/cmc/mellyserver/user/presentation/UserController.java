package cmc.mellyserver.user.presentation;

import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memoryScrap.application.MemoryScrapService;
import cmc.mellyserver.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import cmc.mellyserver.memoryScrap.presentation.dto.MemoryScrapResponseWrapper;
import cmc.mellyserver.place.domain.enums.ScrapType;
import cmc.mellyserver.placeScrap.application.PlaceScrapService;
import cmc.mellyserver.placeScrap.application.dto.PlaceScrapResponseDto;
import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.application.dto.GroupMemory;
import cmc.mellyserver.user.application.dto.PollRecommendResponse;
import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponse;
import cmc.mellyserver.user.presentation.dto.common.*;
import cmc.mellyserver.user.presentation.dto.request.ParticipateGroupRequest;
import cmc.mellyserver.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.user.presentation.dto.response.GetUserMemoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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



    @Operation(summary = "유저 ID로 유저 정보 가져오기")
    @GetMapping("/{userSeq}")
    public ResponseEntity<CommonResponse> getUserNickname(@PathVariable Long userSeq)
    {
        String nickname = userService.getUserNickname(userSeq);
        return ResponseEntity.ok(new CommonResponse(200,"성공",new CommonDetailResponse<>(nickname)));
    }



    @Operation(summary = "유저 회원가입시 설문조사")
    @PostMapping("/survey")
    public ResponseEntity<CommonResponse> addSurvey(@AuthenticationPrincipal User user,@RequestBody SurveyRequest surveyRequest)
    {
        userService.createSurvey(user.getUsername(),surveyRequest);
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }



    @Operation(summary = "설문조사 결과로 추천 장소 조회")
    @GetMapping("/survey")
    public ResponseEntity<CommonResponse> getPoll(@AuthenticationPrincipal User user)
    {
        PollRecommendResponse result = userService.getSurvey(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"성공", new SurveyRecommendResponseWrapper(result)));
    }



    @Operation(summary = "프로필 수정 폼에 필요한 유저 정보 조회")
    @GetMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfileFormData(@AuthenticationPrincipal User user)
    {
        ProfileUpdateFormResponse profileDataForUpdate = userService.getProfileDataForUpdate(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"유저 프로필 수정을 위한 폼 정보 조회",new ProfileUpdateFormResponseWrapper(profileDataForUpdate)));
    }



    @Operation(summary = "프로필 정보 수정 기능",description = "프로필 정보 수정할때 닉네임 중복 체크의 경우에는 기존 인증 로직에서 사용한 중복 체크 재활용하면 될 것 같아요~")
    @PutMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@AuthenticationPrincipal User user, ProfileUpdateRequest profileUpdateRequest)
    {
        userService.updateProfile(user.getUsername(), profileUpdateRequest);
        return ResponseEntity.ok(new CommonResponse(200, "프로필 수정 완료"));
    }



    /**
     * 마이페이지 - My 메모리
     */
    @Operation(summary = "유저가 작성한 메모리 조회")
    @GetMapping("/memory")
    public ResponseEntity<CommonResponse> getUserMemory( @AuthenticationPrincipal User user,
                                                         @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC,size = 10) Pageable pageable,
                                                         @RequestParam(required = false) GroupType groupType )
    {
        Slice<GetUserMemoryResponse> userMemory = userService.getUserMemory(pageable, user.getUsername(), groupType);
        return ResponseEntity.ok(new CommonResponse(200, "유저가 작성한 메모리 조회", new GetUserMemoryResponseWrapper(userMemory)));
    }



    /**
     * 마이페이지 - My 그룹
     */
    @Operation(summary = "유저가 속해있는 그룹 조회")
    @GetMapping("/group")
    public ResponseEntity<CommonResponse> getUserGroup(@AuthenticationPrincipal User user)
    {
        List<UserGroup> userGroup = userService.getUserGroup(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200, "성공", new GetUserGroupResponseWrapper(UserAssembler.getUserGroupResponses(userGroup,user.getUsername()))));
    }



    /**
     * 마이페이지 - My 그룹 - 내 그룹의 메모리 모두 조회
     */
    @Operation(summary = "유저가 속해 있는 그룹의 메모리 조회",description = "유저의 그룹 내 구성원들이 해당 그룹을 대상으로 그룹공개/전체공개로 작성한 메모리 목록입니다")
    @GetMapping("/group/{groupId}/memory")
    public ResponseEntity<CommonResponse> getMemoryBelongToMyGroup(@AuthenticationPrincipal User user, Pageable pageable, @PathVariable Long groupId, @RequestParam(required = false,name = "userId") Long uid)
    {
        Slice<GroupMemory> results = userService.getMemoryBelongToMyGroup(pageable, groupId,user.getUsername(),uid);
        return ResponseEntity.ok(new CommonResponse(200,"유저가 속해있는 그룹의 메모리 조회",results));
    }



    /**
     * 마이페이지 - My 장소 스크랩 -> 스크랩 타입 별 장소 개수 조회
     */
    @Operation(summary = "유저가 스크랩한 장소 타입 별 개수 조회")
    @GetMapping("/place/scrap/count")
    public ResponseEntity<CommonResponse> getPlaceUserScrapCount(@AuthenticationPrincipal User user)
    {
        List<PlaceScrapResponseDto> scrapedPlaceGroup = placeScrapService.getScrapedPlaceCount(user.getUsername());
        return  ResponseEntity.ok(new CommonResponse(200,"스크랩 타입 별 스크랩 개수 조회",new PlaceScrapResponseWrapper(scrapedPlaceGroup)));
    }



    /**
     * 마이페이지 - My 장소 스크랩 -> 스크랩 타입 별 장소 조회
     */
    @Operation(summary = "유저가 스크랩한 장소 타입 별 조회")
    @GetMapping("/place/scrap")
    public ResponseEntity<CommonResponse> getPlaceUserScrap(@AuthenticationPrincipal User user, Pageable pageable, @RequestParam(required = false) ScrapType scrapType)
    {
        Slice<ScrapedPlaceResponseDto> scrapedPlace = placeScrapService.getScrapedPlace(pageable, user.getUsername(), scrapType);
        return  ResponseEntity.ok(new CommonResponse(200,"스크랩 타입 별 스크랩 조회",new ScrapPlaceResponseWrapper(scrapedPlace)));
    }



    /**
     * 마이페이지 - My 메모리 스크랩 (데모데이 이후에 추가)
     */
    @Operation(summary = "유저가 스크랩한 메모리 조회")
    @GetMapping("/memory/scrap")
    public ResponseEntity<CommonResponse> getMemoryUserScrap(@AuthenticationPrincipal User user, Pageable pageable,@RequestParam(required = false) GroupType groupType)
    {
        Slice<ScrapedMemoryResponseDto> scrapedMemory = memoryScrapService.getScrapedMemory(pageable, user.getUsername(),groupType);
        return ResponseEntity.ok(new CommonResponse(200, "유저가 스크랩한 메모리 목록", new MemoryScrapResponseWrapper(scrapedMemory)));
    }



    /**
     * 유저 이미지 용량 조회
     */
    @Operation(summary = "유저가 저장한 이미지 용량 조회")
    @GetMapping("/volume")
    public ResponseEntity<CommonResponse> getUserImageVolume(@AuthenticationPrincipal User user)
    {
        int volume = userService.checkUserImageVolume(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200, "유저가 저장한 사진 총 용량", new UserImageVolumeWrapper(volume)));
    }



    @Operation(summary = "초대링크를 받은 후 그룹에 참여")
    @PostMapping("/group")
    public ResponseEntity<CommonResponse> participateToGroup(@AuthenticationPrincipal User user, @RequestBody ParticipateGroupRequest participateGroupRequest)
    {
        userService.participateToGroup(user.getUsername(), participateGroupRequest.getGroupId());
        return ResponseEntity.ok(new CommonResponse(200, "성공"));
    }

}
