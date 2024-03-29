package cmc.mellyserver.controller.user;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.auth.common.resolver.CurrentUser;
import cmc.mellyserver.auth.common.resolver.LoginUser;
import cmc.mellyserver.controller.user.dto.UserAssembler;
import cmc.mellyserver.controller.user.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.controller.user.dto.response.PlaceScrapCountResponse;
import cmc.mellyserver.controller.user.dto.response.ProfileResponse;
import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.scrap.ScrapType;
import cmc.mellyserver.domain.group.GroupService;
import cmc.mellyserver.domain.group.dto.response.UserJoinedGroupsResponse;
import cmc.mellyserver.domain.memory.MemoryService;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.scrap.PlaceScrapService;
import cmc.mellyserver.domain.scrap.dto.response.ScrapedPlaceListResponse;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.domain.user.UserProfileService;
import cmc.mellyserver.domain.user.dto.response.ProfileResponseDto;
import cmc.mellyserver.support.response.ApiResponse;
import cmc.mellyserver.support.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final PlaceScrapService placeScrapService;

    private final UserProfileService userProfileService;

    private final MemoryService memoryService;

    private final GroupService groupService;

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getUserProfile(@CurrentUser LoginUser loginUser) {

        ProfileResponseDto profileResponseDto = userProfileService.getProfile(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, ProfileResponse.of(profileResponseDto));
    }

    @PatchMapping("/my-profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@CurrentUser LoginUser loginUser,
        @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {

        userProfileService.updateProfile(loginUser.getId(), profileUpdateRequest.toServiceRequest());
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    @PatchMapping("/my-profile/profile-image")
    public ResponseEntity<ApiResponse<Void>> updateProfileImage(@CurrentUser LoginUser loginUser,
        MultipartFile profileImage) throws IOException {

        userProfileService.updateProfileImage(loginUser.getId(), profileImage, true);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    @GetMapping("/my-memories")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getUserMemories(@CurrentUser LoginUser loginUser,
        @RequestParam Long lastId, @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(required = false) GroupType groupType) {

        MemoryListResponse memoryListResponse = memoryService.getUserMemories(lastId, loginUser.getId(), null,
            groupType, pageable);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, memoryListResponse);
    }

    @GetMapping("/my-groups")
    public ResponseEntity<ApiResponse<UserJoinedGroupsResponse>> getUserGroup(
        @CurrentUser LoginUser loginUser, @RequestParam Long lastId,
        @PageableDefault(size = 10) Pageable pageable) {

        UserJoinedGroupsResponse groupListLoginUserParticiated = groupService
            .findUserParticipatedGroups(loginUser.getId(), lastId, pageable);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, groupListLoginUserParticiated);
    }

    @GetMapping("/my-groups/{groupId}/memories")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getMemoryBelongToMyGroup(
        @RequestParam Long lastId, @PageableDefault(size = 10) Pageable pageable,
        @PathVariable Long groupId) {

        MemoryListResponse memoryListResponse = memoryService.getGroupMemoriesById(lastId, groupId, pageable);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, memoryListResponse);
    }

    @GetMapping("/place-scraps/count")
    public ResponseEntity<ApiResponse<List<PlaceScrapCountResponse>>> getPlaceUserScrapCount(
        @CurrentUser LoginUser loginUser) {

        List<PlaceScrapCountResponseDto> results = placeScrapService.countByPlaceScrapType(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, UserAssembler.placeScrapCountResponses(results));
    }

    @GetMapping("/place-scraps")
    public ResponseEntity<ApiResponse<ScrapedPlaceListResponse>> getPlaceUserScrap(@CurrentUser LoginUser loginUser,
        @RequestParam Long lastId, @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(required = false) ScrapType scrapType) {

        ScrapedPlaceListResponse scrapedPlace = placeScrapService.findScrapedPlace(lastId, pageable, loginUser.getId(),
            scrapType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, scrapedPlace);
    }
}