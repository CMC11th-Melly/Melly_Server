package cmc.mellyserver.mellyapi.user.presentation;

import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.MemoryAssembler;
import cmc.mellyserver.mellyapi.user.presentation.dto.UserAssembler;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.ParticipateGroupRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.group.application.GroupService;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.application.MemoryReadService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.CREATED;
import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.OK;
import static cmc.mellyserver.mellyapi.common.response.ApiResponse.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final PlaceScrapService placeScrapService;

    private final UserProfileService userProfileService;

    private final UserSurveyService userSurveyService;

    private final MemoryReadService memoryService;

    private final GroupService groupService;

    @GetMapping("/{userSeq}/nickname")
    public ResponseEntity<ApiResponse> getUserNickname(@PathVariable Long userSeq) {

        String nickname = userProfileService.findNicknameByUserIdentifier(userSeq);
        return OK(nickname);
    }

    @PostMapping("/surveys")
    public ResponseEntity<Void> addSurvey(@AuthenticationPrincipal User user,
                                          @RequestBody SurveyRequest surveyRequest) {

        userSurveyService.createSurvey(
                UserAssembler.surveyRequestDto(Long.parseLong(user.getUsername()), surveyRequest));
        return CREATED;
    }

    @GetMapping("/surveys")
    public ResponseEntity<ApiResponse> getSurvey(@AuthenticationPrincipal User user) {

        SurveyRecommendResponseDto surveyRecommendResponseDto = userSurveyService.getSurveyResult(
                Long.parseLong(user.getUsername()));
        return OK(surveyRecommendResponseDto);
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse> updateProfileFormData(@AuthenticationPrincipal User user) {

        ProfileUpdateFormResponseDto profileUpdateFormResponseDto = userProfileService.getLoginUserProfileDataForUpdate(
                Long.parseLong(user.getUsername()));
        return OK(profileUpdateFormResponseDto);
    }

    @PutMapping("/my-profile")
    public ResponseEntity<ApiResponse> updateProfile(@AuthenticationPrincipal User user,
                                                     ProfileUpdateRequest profileUpdateRequest) {

        userProfileService.updateLoginUserProfile(
                UserAssembler.profileUpdateRequestDto(Long.parseLong(user.getUsername()),
                        profileUpdateRequest));
        return OK;
    }

    @GetMapping("/my-memorys")
    public ResponseEntity<ApiResponse> getUserMemory(@AuthenticationPrincipal User user,
                                                     @PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
                                                     @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> results = memoryService.findMemoriesLoginUserWrite(pageable,
                Long.parseLong(user.getUsername()), groupType);
        return OK(results);
    }

    @GetMapping("/my-groups")
    public ResponseEntity<ApiResponse> getUserGroup(@AuthenticationPrincipal User user) {

        List<GroupLoginUserParticipatedResponseDto> results = groupService.findGroupListLoginUserParticiated(
                Long.parseLong(user.getUsername()));
        return OK(UserAssembler.groupLoginUserParticipatedResponses(results));
    }

    @GetMapping("/my-group/{groupId}/memorys")
    public ResponseEntity<ApiResponse> getMemoryBelongToMyGroup(Pageable pageable,
                                                                @PathVariable Long groupId, @RequestParam(required = false, name = "userId") Long userSeq) {

        Slice<MemoryResponseDto> results = memoryService.findMemoriesUsersBelongToMyGroupWrite(
                pageable, groupId, userSeq);
        return OK(MemoryAssembler.memoryResponses(results));
    }

    @GetMapping("/place-scraps/count")
    public ResponseEntity<ApiResponse> getPlaceUserScrapCount(@AuthenticationPrincipal User user) {

        List<PlaceScrapCountResponseDto> results = placeScrapService.countByPlaceScrapType(
                Long.parseLong(user.getUsername()));
        return OK(UserAssembler.placeScrapCountResponses(results));
    }

    @GetMapping("/place-scraps")
    public ResponseEntity<ApiResponse> getPlaceUserScrap(@AuthenticationPrincipal User user,
                                                         Pageable pageable, @RequestParam(required = false) ScrapType scrapType) {

        Slice<ScrapedPlaceResponseDto> results = placeScrapService.findScrapedPlace(pageable,
                Long.parseLong(user.getUsername()), scrapType);
        return OK(UserAssembler.scrapedPlaceResponses(results));
    }

    @GetMapping("/volume")
    public ResponseEntity<ApiResponse> getUserImageVolume(@AuthenticationPrincipal User user) {

        Integer volume = userProfileService.checkImageStorageVolumeLoginUserUse(user.getUsername());
        return OK(volume);
    }

    @PostMapping("/groups/participate")
    public ResponseEntity<Void> participateToGroup(@AuthenticationPrincipal User user,
                                                   @RequestBody ParticipateGroupRequest participateGroupRequest) {

        groupService.participateToGroup(Long.parseLong(user.getUsername()),
                participateGroupRequest.getGroupId());
        return CREATED;
    }
}
