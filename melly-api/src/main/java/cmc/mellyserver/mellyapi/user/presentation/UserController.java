package cmc.mellyserver.mellyapi.user.presentation;

import cmc.mellyserver.mellyapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.common.MemoryAssembler;
import cmc.mellyserver.mellyapi.user.presentation.dto.UserAssembler;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.ParticipateGroupRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.group.application.GroupService;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.application.MemoryService;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellycore.user.application.UserProfileService;
import cmc.mellyserver.mellycore.user.application.UserSurveyService;
import cmc.mellyserver.mellycore.user.application.dto.SurveyRecommendResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateFormResponseDto;
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

    private final UserProfileService userProfileService;

    private final UserSurveyService userSurveyService;

    private final MemoryService memoryService;

    private final GroupService groupService;

    @GetMapping("/{userSeq}")
    public ResponseEntity<ApiResponse> getUserNickname(@PathVariable Long userSeq) {

        String nickname = userProfileService.findNicknameByUserIdentifier(userSeq);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, nickname));
    }

    @PostMapping("/survey")
    public ResponseEntity<ApiResponse> addSurvey(@AuthenticationPrincipal User user, @RequestBody SurveyRequest surveyRequest) {

        userSurveyService.createSurvey(UserAssembler.surveyRequestDto(Long.parseLong(user.getUsername()), surveyRequest));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @GetMapping("/survey")
    public ResponseEntity<ApiResponse> getSurvey(@AuthenticationPrincipal User user) {

        SurveyRecommendResponseDto surveyRecommendResponseDto = userSurveyService.getSurveyResult(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, surveyRecommendResponseDto));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfileFormData(@AuthenticationPrincipal User user) {

        ProfileUpdateFormResponseDto profileUpdateFormResponseDto = userProfileService.getLoginUserProfileDataForUpdate(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, profileUpdateFormResponseDto));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(@AuthenticationPrincipal User user, ProfileUpdateRequest profileUpdateRequest) {

        userProfileService.updateLoginUserProfile(UserAssembler.profileUpdateRequestDto(Long.parseLong(user.getUsername()), profileUpdateRequest));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @GetMapping("/memory")
    public ResponseEntity<ApiResponse> getUserMemory(@AuthenticationPrincipal User user, @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable, @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> results = memoryService.findMemoriesLoginUserWrite(pageable, Long.parseLong(user.getUsername()), groupType);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                MemoryAssembler.memoryResponses(results)));
    }

    @GetMapping("/group")
    public ResponseEntity<ApiResponse> getUserGroup(@AuthenticationPrincipal User user) throws InterruptedException {

        List<GroupLoginUserParticipatedResponseDto> results = groupService.findGroupListLoginUserParticiated(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, UserAssembler.groupLoginUserParticipatedResponses(results)));
    }

    @GetMapping("/group/{groupId}/memory")
    public ResponseEntity<ApiResponse> getMemoryBelongToMyGroup(Pageable pageable, @PathVariable Long groupId, @RequestParam(required = false, name = "userId") Long userSeq) {

        Slice<MemoryResponseDto> results = memoryService.findMemoriesUsersBelongToMyGroupWrite(pageable, groupId, userSeq);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                MemoryAssembler.memoryResponses(results)));
    }

    @GetMapping("/place/scrap/count")
    public ResponseEntity<ApiResponse> getPlaceUserScrapCount(@AuthenticationPrincipal User user) {

        List<PlaceScrapCountResponseDto> results = placeScrapService.countByPlaceScrapType(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, UserAssembler.placeScrapCountResponses(results)));
    }

    @GetMapping("/place/scrap")
    public ResponseEntity<ApiResponse> getPlaceUserScrap(@AuthenticationPrincipal User user, Pageable pageable, @RequestParam(required = false) ScrapType scrapType) {

        Slice<ScrapedPlaceResponseDto> results = placeScrapService.findScrapedPlace(pageable, Long.parseLong(user.getUsername()), scrapType);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
                UserAssembler.scrapedPlaceResponses(results)));
    }

    @GetMapping("/volume")
    public ResponseEntity<ApiResponse> getUserImageVolume(@AuthenticationPrincipal User user) {

        Integer volume = userProfileService.checkImageStorageVolumeLoginUserUse(user.getUsername());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, volume));
    }

    @PostMapping("/group")
    public ResponseEntity<ApiResponse> participateToGroup(@AuthenticationPrincipal User user, @RequestBody ParticipateGroupRequest participateGroupRequest) {

        groupService.participateToGroup(Long.parseLong(user.getUsername()), participateGroupRequest.getGroupId());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }
}
