package cmc.mellyserver.controller.user;

import cmc.mellyserver.common.code.SuccessCode;
import cmc.mellyserver.controller.auth.dto.common.CurrentUser;
import cmc.mellyserver.controller.auth.dto.common.LoginUser;
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

    private final MemoryReadService memoryService;

    private final GroupService groupService;


    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getUserProfile(@CurrentUser LoginUser loginUser) {

        ProfileResponseDto profileResponseDto = userProfileService.getProfile(loginUser.getId());
        Integer volume = userProfileService.checkImageStorageVolume(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, ProfileResponse.of(profileResponseDto, volume));
    }


    @PatchMapping("/my-profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@CurrentUser LoginUser loginUser, @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {

        userProfileService.updateProfile(loginUser.getId(), profileUpdateRequest.toServiceRequest());
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }

    @PatchMapping("/my-profile/profile-image")
    public ResponseEntity<ApiResponse<Void>> updateProfileImage(@CurrentUser LoginUser loginUser, MultipartFile profileImage) throws IOException {

        userProfileService.updateProfileImage(loginUser.getId(), profileImage);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }


    @GetMapping("/my-memories")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getUserMemory(@CurrentUser LoginUser loginUser,
                                                                         @RequestParam(required = false) Long lastId,
                                                                         @PageableDefault(size = 10) Pageable pageable,
                                                                         @RequestParam(required = false) String groupType) {

        MemoryListResponse memoryListResponse = memoryService.findMemoriesLoginUserWrite(lastId, pageable, loginUser.getId(), GroupType.from(groupType));
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, memoryListResponse);
    }


    @GetMapping("/my-groups")
    public ResponseEntity<ApiResponse<GroupListLoginUserParticipatedResponse>> getUserGroup(@CurrentUser LoginUser loginUser,
                                                                                            @RequestParam(name = "lastId", required = false) Long lastId,
                                                                                            @PageableDefault(size = 10) Pageable pageable) {

        GroupListLoginUserParticipatedResponse groupListLoginUserParticiated = groupService.findGroupListLoginUserParticiated(loginUser.getId(), lastId, pageable);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, groupListLoginUserParticiated);
    }


    @GetMapping("/my-groups/{groupId}/memorys")
    public ResponseEntity<ApiResponse<MemoryListResponse>> getMemoryBelongToMyGroup(@CurrentUser LoginUser loginUser,
                                                                                    @RequestParam(name = "lastId", required = false) Long lastId,
                                                                                    @PageableDefault(size = 10) Pageable pageable,
                                                                                    @PathVariable Long groupId,
                                                                                    @RequestParam(required = false) String groupType) {

        MemoryListResponse memoryListResponse = memoryService.findMemoriesUsersBelongToMyGroupWrite(lastId, pageable, groupId, loginUser.getId(), GroupType.from(groupType));
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, memoryListResponse);
    }


    @GetMapping("/place-scraps/count")
    public ResponseEntity<ApiResponse<List<PlaceScrapCountResponse>>> getPlaceUserScrapCount(@CurrentUser LoginUser loginUser) {

        List<PlaceScrapCountResponseDto> results = placeScrapService.countByPlaceScrapType(loginUser.getId());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, UserAssembler.placeScrapCountResponses(results));
    }


    @GetMapping("/place-scraps")
    public ResponseEntity<ApiResponse<ScrapedPlaceListResponse>> getPlaceUserScrap(@CurrentUser LoginUser loginUser,
                                                                                   @RequestParam(required = false) Long lastId,
                                                                                   @PageableDefault(size = 10) Pageable pageable,
                                                                                   @RequestParam(required = false) String scrapType) {

        ScrapedPlaceListResponse scrapedPlace = placeScrapService.findScrapedPlace(lastId, pageable, loginUser.getId(), ScrapType.from(scrapType));
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, scrapedPlace);
    }
}