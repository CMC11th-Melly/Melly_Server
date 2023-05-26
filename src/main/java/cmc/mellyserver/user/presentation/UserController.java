package cmc.mellyserver.user.presentation;

import cmc.mellyserver.common.constants.MessageConstant;
import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.scrap.application.PlaceScrapService;
import cmc.mellyserver.scrap.application.dto.response.PlaceScrapCountResponseDto;
import cmc.mellyserver.scrap.application.dto.response.ScrapedPlaceResponseDto;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.application.dto.SurveyRecommendResponseDto;
import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponseDto;
import cmc.mellyserver.user.presentation.dto.request.*;
import cmc.mellyserver.user.presentation.dto.response.GroupLoginUserParticipatedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

    private final UserService userService;


    @GetMapping("/{userSeq}")
    public ResponseEntity<CommonResponse> getUserNickname(@PathVariable Long userSeq)
    {
        String nickname = userService.findNicknameByUserIdentifier(userSeq);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(nickname)));
    }


    @PostMapping("/survey")
    public ResponseEntity<CommonResponse> addSurvey(@AuthenticationPrincipal User user, @RequestBody SurveyRequest surveyRequest)
    {
        userService.createSurvey(SurveyRequestDto.of(Long.parseLong(user.getUsername()),surveyRequest));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @GetMapping("/survey")
    public ResponseEntity<CommonResponse> getSurvey(@AuthenticationPrincipal User user)
    {
        SurveyRecommendResponseDto surveyRecommendResponseDto = userService.getSurveyResult(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new CommonDetailResponse<>(surveyRecommendResponseDto)));
    }


    @GetMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfileFormData(@AuthenticationPrincipal User user)
    {
        ProfileUpdateFormResponseDto profileUpdateFormResponseDto = userService.getLoginUserProfileDataForUpdate(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(profileUpdateFormResponseDto)));
    }


    @PutMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@AuthenticationPrincipal User user, ProfileUpdateRequest profileUpdateRequest)
    {
        userService.updateLoginUserProfile(ProfileUpdateRequestDto.of(Long.parseLong(user.getUsername()), profileUpdateRequest));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @GetMapping("/memory")
    public ResponseEntity<CommonResponse> getUserMemory( @AuthenticationPrincipal User user,
                                                         @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC,size = 10) Pageable pageable,
                                                         @RequestParam(required = false) GroupType groupType )
    {
        Slice<MemoryResponseDto> userMemory = userService.findMemoriesLoginUserWrite(pageable, Long.parseLong(user.getUsername()), groupType);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new CommonDetailResponse<>(userMemory)));
    }


    @GetMapping("/group")
    public ResponseEntity<CommonResponse> getUserGroup(@AuthenticationPrincipal User user)
    {
        List<GroupLoginUserParticipatedResponseDto> userGroup = userService.findGroupListLoginUserParticiated(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new CommonDetailResponse<>(userGroup)));
    }


    @GetMapping("/group/{groupId}/memory")
    public ResponseEntity<CommonResponse> getMemoryBelongToMyGroup(Pageable pageable, @PathVariable Long groupId, @RequestParam(required = false,name = "userId") Long userSeq)
    {
        Slice<MemoryResponseDto> results = userService.findMemoriesUsersBelongToMyGroupWrite(pageable, groupId, userSeq);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,results));
    }


    @GetMapping("/place/scrap/count")
    public ResponseEntity<CommonResponse> getPlaceUserScrapCount(@AuthenticationPrincipal User user)
    {
        List<PlaceScrapCountResponseDto> scrapedPlaceGroup = placeScrapService.countByPlaceScrapType(Long.parseLong(user.getUsername()));
        return  ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(scrapedPlaceGroup)));
    }


    @GetMapping("/place/scrap")
    public ResponseEntity<CommonResponse> getPlaceUserScrap(@AuthenticationPrincipal User user, Pageable pageable, @RequestParam(required = false) ScrapType scrapType)
    {
        Slice<ScrapedPlaceResponseDto> scrapedPlace = placeScrapService.findScrapedPlace(pageable, Long.parseLong(user.getUsername()), scrapType);
        return  ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(scrapedPlace)));
    }


    @GetMapping("/volume")
    public ResponseEntity<CommonResponse> getUserImageVolume(@AuthenticationPrincipal User user)
    {
        Integer volume = userService.checkImageStorageVolumeLoginUserUse(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse<>(volume)));
    }


    @PostMapping("/group")
    public ResponseEntity<CommonResponse> participateToGroup(@AuthenticationPrincipal User user, @RequestBody ParticipateGroupRequest participateGroupRequest)
    {
        userService.participateToGroup(Long.parseLong(user.getUsername()), participateGroupRequest.getGroupId());
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

}
