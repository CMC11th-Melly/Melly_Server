package cmc.mellyserver.controller.user;

import cmc.mellyserver.auth.controller.dto.common.CurrentUser;
import cmc.mellyserver.auth.controller.dto.common.LoginUser;
import cmc.mellyserver.common.code.SuccessCode;
import cmc.mellyserver.controller.user.dto.UserAssembler;
import cmc.mellyserver.controller.user.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.controller.user.dto.response.PlaceScrapCountResponse;
import cmc.mellyserver.controller.user.dto.response.ProfileResponse;
import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.scrap.enums.ScrapType;
import cmc.mellyserver.domain.group.GroupService;
import cmc.mellyserver.domain.group.dto.response.GroupListLoginUserParticipatedResponse;
import cmc.mellyserver.domain.memory.MemoryReadService;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.scrap.PlaceScrapService;
import cmc.mellyserver.domain.scrap.dto.response.ScrapedPlaceListResponse;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.domain.user.UserProfileService;
import cmc.mellyserver.domain.user.dto.response.ProfileResponseDto;
import cmc.mellyserver.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final PlaceScrapService placeScrapService;

    private final UserProfileService userProfileService;

    private final MemoryReadService memoryReadService;

    private final GroupService groupService;

    // 내 프로필 조회
    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getUserProfile(@CurrentUser LoginUser loginUser) {

        ProfileResponseDto profileResponseDto = userProfileService.getProfile(loginUser.getId());
        Integer volume = userProfileService.checkImageStorageVolume(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, ProfileResponse.of(profileResponseDto, volume));
    }

    // 내 프로필 수정
    @PatchMapping("/my-profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@CurrentUser LoginUser loginUser, @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {

        userProfileService.updateProfile(loginUser.getId(), profileUpdateRequest.toServiceRequest());
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    // 내 프로필 이미지 수정
    @PatchMapping("/my-profile/profile-image")
    public ResponseEntity<ApiResponse<Void>> updateProfileImage(@CurrentUser LoginUser loginUser, MultipartFile profileImage) throws IOException {

        userProfileService.updateProfileImage(loginUser.getId(), profileImage);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    // 내가 작성한 메모리 조회
    @GetMapping("/my-memories")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getUserMemory(@CurrentUser LoginUser loginUser,
                                                                         @RequestParam(required = false) Long lastId,
                                                                         @PageableDefault(size = 10) Pageable pageable,
                                                                         @RequestParam(required = false) String groupType) {

        MemoryListResponse memoryListResponse = memoryReadService.findMemoriesLoginUserWrite(lastId, pageable, loginUser.getId(), GroupType.from(groupType));
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, memoryListResponse);
    }

    // 내가 포함된 그룹 조회
    @GetMapping("/my-groups")
    public ResponseEntity<ApiResponse<GroupListLoginUserParticipatedResponse>> getUserGroup(@CurrentUser LoginUser loginUser,
                                                                                            @RequestParam(name = "lastId", required = false) Long lastId,
                                                                                            @PageableDefault(size = 10) Pageable pageable) {

        GroupListLoginUserParticipatedResponse groupListLoginUserParticiated = groupService.findGroupListLoginUserParticiated(loginUser.getId(), lastId, pageable);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, groupListLoginUserParticiated);
    }

    // 내 그룹이 작성한 메모리 조회
    @GetMapping("/my-groups/{groupId}/memories")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getMemoryBelongToMyGroup(@CurrentUser LoginUser loginUser,
                                                                                    @RequestParam(name = "lastId", required = false) Long lastId,
                                                                                    @PageableDefault(size = 10) Pageable pageable,
                                                                                    @PathVariable Long groupId,
                                                                                    @RequestParam(required = false) String groupType) {

        MemoryListResponse memoryListResponse = memoryReadService.findMemoriesUsersBelongToMyGroupWrite(lastId, pageable, groupId, loginUser.getId(), GroupType.from(groupType));
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, memoryListResponse);
    }

    // 내가 스크랩한 장소 스크랩 타입별 개수 조회
    @GetMapping("/place-scraps/count")
    public ResponseEntity<ApiResponse<List<PlaceScrapCountResponse>>> getPlaceUserScrapCount(@CurrentUser LoginUser loginUser) {

        List<PlaceScrapCountResponseDto> results = placeScrapService.countByPlaceScrapType(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, UserAssembler.placeScrapCountResponses(results));
    }

    // 내가 스크랩한 장소 조회
    @GetMapping("/place-scraps")
    public ResponseEntity<ApiResponse<ScrapedPlaceListResponse>> getPlaceUserScrap(@CurrentUser LoginUser loginUser,
                                                                                   @RequestParam(required = false) Long lastId,
                                                                                   @PageableDefault(size = 10) Pageable pageable,
                                                                                   @RequestParam(required = false) String scrapType) {

        ScrapedPlaceListResponse scrapedPlace = placeScrapService.findScrapedPlace(lastId, pageable, loginUser.getId(), ScrapType.from(scrapType));
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, scrapedPlace);
    }
}