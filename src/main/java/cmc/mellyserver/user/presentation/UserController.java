package cmc.mellyserver.user.presentation;

import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.memory.infrastructure.data.dto.MemoryResponseDto;
import cmc.mellyserver.scrap.application.PlaceScrapService;
import cmc.mellyserver.scrap.application.dto.response.PlaceScrapCountResponseDto;
import cmc.mellyserver.scrap.application.dto.response.ScrapedPlaceResponseDto;
import cmc.mellyserver.user.application.impl.UserServiceImpl;
import cmc.mellyserver.user.application.dto.SurveyRecommendResponseDto;
import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponseDto;
import cmc.mellyserver.user.presentation.dto.common.*;
import cmc.mellyserver.user.presentation.dto.request.ParticipateGroupRequest;
import cmc.mellyserver.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.user.presentation.dto.request.ProfileUpdateRequestDto;
import cmc.mellyserver.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.user.presentation.dto.response.GroupLoginUserParticipatedResponseDto;
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

    private final UserServiceImpl userService;



    @Operation(summary = "유저 식별자로 유저 정보 가져오기")
    @GetMapping("/{userSeq}")
    public ResponseEntity<CommonResponse> getUserNickname(@PathVariable Long userSeq)
    {
        String nickname = userService.findNicknameByUserIdentifier(userSeq);
        return ResponseEntity.ok(new CommonResponse(200,"성공",new CommonDetailResponse<>(nickname)));
    }



    @Operation(summary = "유저 회원가입시 설문조사")
    @PostMapping("/survey")
    public ResponseEntity<CommonResponse> addSurvey(@AuthenticationPrincipal User user,@RequestBody SurveyRequest surveyRequest)
    {
        userService.createSurvey(Long.parseLong(user.getUsername()),surveyRequest);
        return ResponseEntity.ok(new CommonResponse(200,"성공"));
    }




    @Operation(summary = "설문조사 결과로 추천 장소 조회")
    @GetMapping("/survey")
    public ResponseEntity<CommonResponse> getSurvey(@AuthenticationPrincipal User user)
    {
        SurveyRecommendResponseDto result = userService.getSurveyResult(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(200,"성공", new SurveyRecommendResponseWrapper(result)));
    }



    @Operation(summary = "프로필 수정 폼에 필요한 유저 정보 조회")
    @GetMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfileFormData(@AuthenticationPrincipal User user)
    {
        ProfileUpdateFormResponseDto profileDataForUpdate = userService.getLoginUserProfileDataForUpdate(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(200,"유저 프로필 수정을 위한 폼 정보 조회",new ProfileUpdateFormResponseWrapper(profileDataForUpdate)));
    }



    @Operation(summary = "프로필 정보 수정 기능")
    @PutMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@AuthenticationPrincipal User user, ProfileUpdateRequest profileUpdateRequest)
    {
        userService.updateLoginUserProfile(ProfileUpdateRequestDto.of(Long.parseLong(user.getUsername()), profileUpdateRequest));
        return ResponseEntity.ok(new CommonResponse(200, "프로필 수정 완료"));
    }



    @Operation(summary = "[마이페이지] 유저가 작성한 메모리 조회")
    @GetMapping("/memory")
    public ResponseEntity<CommonResponse> getUserMemory( @AuthenticationPrincipal User user,
                                                         @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC,size = 10) Pageable pageable,
                                                         @RequestParam(required = false) GroupType groupType )
    {
        Slice<MemoryResponseDto> userMemory = userService.findMemoriesLoginUserWrite(pageable, Long.parseLong(user.getUsername()), groupType);
        return ResponseEntity.ok(new CommonResponse(200, "유저가 작성한 메모리 조회", new GetUserMemoryResponseWrapper(userMemory)));
    }



    @Operation(summary = "[마이페이지] 유저가 속해있는 그룹 조회")
    @GetMapping("/group")
    public ResponseEntity<CommonResponse> getUserGroup(@AuthenticationPrincipal User user)
    {
        List<GroupLoginUserParticipatedResponseDto> userGroup = userService.findGroupListLoginUserParticiated(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(200, "성공", new GetUserGroupResponseWrapper(userGroup)));
    }



    @Operation(summary = "유저가 속해 있는 그룹의 메모리 조회",description = "유저의 그룹 내 구성원들이 해당 그룹을 대상으로 그룹공개/전체공개로 작성한 메모리 목록입니다")
    @GetMapping("/group/{groupId}/memory")
    public ResponseEntity<CommonResponse> getMemoryBelongToMyGroup(Pageable pageable, @PathVariable Long groupId, @RequestParam(required = false,name = "userId") Long userSeq)
    {
        Slice<MemoryResponseDto> results = userService.findMemoriesUsersBelongToMyGroupWrite(pageable, groupId, userSeq);
        return ResponseEntity.ok(new CommonResponse(200,"유저가 속해있는 그룹의 메모리 조회",results));
    }



    @Operation(summary = "유저가 스크랩한 장소 타입 별 개수 조회")
    @GetMapping("/place/scrap/count")
    public ResponseEntity<CommonResponse> getPlaceUserScrapCount(@AuthenticationPrincipal User user)
    {
        List<PlaceScrapCountResponseDto> scrapedPlaceGroup = placeScrapService.countByPlaceScrapType(Long.parseLong(user.getUsername()));
        return  ResponseEntity.ok(new CommonResponse(200,"스크랩 타입 별 스크랩 개수 조회",new PlaceScrapResponseWrapper(scrapedPlaceGroup)));
    }



    @Operation(summary = "유저가 스크랩한 장소 타입 별 조회")
    @GetMapping("/place/scrap")
    public ResponseEntity<CommonResponse> getPlaceUserScrap(@AuthenticationPrincipal User user, Pageable pageable, @RequestParam(required = false) ScrapType scrapType)
    {
        Slice<ScrapedPlaceResponseDto> scrapedPlace = placeScrapService.findScrapedPlace(pageable, Long.parseLong(user.getUsername()), scrapType);
        return  ResponseEntity.ok(new CommonResponse(200,"스크랩 타입 별 스크랩 조회",new ScrapPlaceResponseWrapper(scrapedPlace)));
    }


   // 최종 확인 완료
    @Operation(summary = "유저가 저장한 이미지 용량 조회")
    @GetMapping("/volume")
    public ResponseEntity<CommonResponse> getUserImageVolume(@AuthenticationPrincipal User user)
    {
        int volume = userService.checkImageStorageVolumeLoginUserUse(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200, "유저가 저장한 사진 총 용량", new UserImageVolumeWrapper(volume)));
    }


    // 최종 확인 완료
    @Operation(summary = "초대링크를 받은 후 그룹에 참여")
    @PostMapping("/group")
    public ResponseEntity<CommonResponse> participateToGroup(@AuthenticationPrincipal User user, @RequestBody ParticipateGroupRequest participateGroupRequest)
    {
        userService.participateToGroup(Long.parseLong(user.getUsername()), participateGroupRequest.getGroupId());
        return ResponseEntity.ok(new CommonResponse(200, "성공"));
    }
}
