package cmc.mellyserver.mellyapi.user.presentation;

import cmc.mellyserver.mellyapi.auth.presentation.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.auth.presentation.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.common.code.SuccessCode;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.MemoryAssembler;
import cmc.mellyserver.mellyapi.user.presentation.dto.UserAssembler;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.ProfileResponse;
import cmc.mellyserver.mellycore.group.application.GroupService;
import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.application.MemoryReadService;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellycore.scrap.domain.enums.ScrapType;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellycore.user.application.UserProfileService;
import cmc.mellyserver.mellycore.user.application.UserSurveyService;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.response.SurveyRecommendResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final PlaceScrapService placeScrapService;

    private final UserProfileService userProfileService;

    private final UserSurveyService userSurveyService;

    private final MemoryReadService memoryService;

    private final GroupService groupService;


    @PostMapping("/surveys")
    public ResponseEntity<ApiResponse> addSurvey(@CurrentUser LoginUser loginUser, @RequestBody SurveyRequest surveyRequest) {

        userSurveyService.createSurvey(loginUser.getId(), surveyRequest.toDto());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }


    @GetMapping("/surveys")
    public ResponseEntity<ApiResponse> getSurvey(@CurrentUser LoginUser loginUser) {

        SurveyRecommendResponseDto surveyRecommendResponseDto = userSurveyService.getSurveyResult(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, surveyRecommendResponseDto);
    }

    // 유저 프로필을 수정에 필요한 데이터 조회
    @GetMapping("/my-profile/edit-form")
    public ResponseEntity<ApiResponse> updateProfileFormData(@CurrentUser LoginUser loginUser) {

        ProfileUpdateFormResponseDto profileUpdateFormResponseDto = userProfileService.getLoginUserProfileDataForUpdate(
                loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, profileUpdateFormResponseDto);
    }

    // 유저 프로필 조회
    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse> getUserProfile(@CurrentUser LoginUser loginUser) {

        ProfileResponseDto profileResponseDto = userProfileService.getUserProfile(loginUser.getId());
        Integer volume = userProfileService.checkImageStorageVolumeLoginUserUse(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, ProfileResponse.of(profileResponseDto, volume));
    }

    // 유저 프로필 수정
    @PatchMapping("/my-profile")
    public ResponseEntity<ApiResponse> updateProfile(@CurrentUser LoginUser loginUser, @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {

        userProfileService.updateUserProfile(loginUser.getId(), profileUpdateRequest.toDto());
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    @PatchMapping("/my-profile/profile-image")
    public ResponseEntity<ApiResponse> updateProfileImage(@CurrentUser LoginUser loginUser, MultipartFile profileImage) throws IOException {

        userProfileService.updateUserProfileImage(loginUser.getId(), profileImage);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    // 내가 작성한 메모리 조회
    @GetMapping("/my-memorys")
    public ResponseEntity<ApiResponse> getUserMemory(@CurrentUser LoginUser loginUser,
                                                     @RequestParam(name = "lastId", required = false) Long lastId,
                                                     @PageableDefault(size = 10) Pageable pageable,
                                                     @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> results = memoryService.findMemoriesLoginUserWrite(lastId, pageable, loginUser.getId(), groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, results);
    }

    // 내가 포함된 그룹 조회
    @GetMapping("/my-groups")
    public ResponseEntity<ApiResponse> getUserGroup(@CurrentUser LoginUser loginUser, @RequestParam(name = "lastId", required = false) Long lastId, @PageableDefault(size = 10) Pageable pageable) {

        Slice<GroupLoginUserParticipatedResponseDto> results = groupService.findGroupListLoginUserParticiated(loginUser.getId(), lastId, pageable);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, UserAssembler.groupLoginUserParticipatedResponses(results));
    }

    // 내가 속해 있는 그룹의 사람들이 작성한 메모리
    @GetMapping("/my-groups/{groupId}/memorys")
    public ResponseEntity<ApiResponse> getMemoryBelongToMyGroup(@CurrentUser LoginUser loginUser, @RequestParam(name = "lastId", required = false) Long lastId, Pageable pageable, @PathVariable Long groupId, @RequestParam(required = false) GroupType groupType) {

        Slice<MemoryResponseDto> results = memoryService.findMemoriesUsersBelongToMyGroupWrite(lastId, pageable, groupId, loginUser.getId(), groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, MemoryAssembler.memoryResponses(results));
    }

    // 내가 스크랩한 장소의 개수 조회
    @GetMapping("/place-scraps/count")
    public ResponseEntity<ApiResponse> getPlaceUserScrapCount(@CurrentUser LoginUser loginUser) {

        List<PlaceScrapCountResponseDto> results = placeScrapService.countByPlaceScrapType(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, UserAssembler.placeScrapCountResponses(results));
    }

    // 스크랩 타입별 조회
    @GetMapping("/place-scraps")
    public ResponseEntity<ApiResponse> getPlaceUserScrap(@CurrentUser LoginUser loginUser, @RequestParam(name = "lastId", required = false) Long lastId, @PageableDefault(size = 10) Pageable pageable, @RequestParam(required = false) ScrapType scrapType) {

        Slice<ScrapedPlaceResponseDto> results = placeScrapService.findScrapedPlace(lastId, pageable, loginUser.getId(), scrapType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, UserAssembler.scrapedPlaceResponses(results));
    }
}
