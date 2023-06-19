package cmc.mellyserver.mellyappexternalapi.user.presentation;

import cmc.mellyserver.mellyappexternalapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyappexternalapi.common.response.CommonResponse;
import cmc.mellyserver.mellyappexternalapi.group.application.GroupService;
import cmc.mellyserver.mellyappexternalapi.memory.presentation.dto.common.MemoryAssembler;
import cmc.mellyserver.mellyappexternalapi.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellyappexternalapi.user.application.UserService;
import cmc.mellyserver.mellyappexternalapi.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.mellyappexternalapi.user.presentation.dto.UserAssembler;
import cmc.mellyserver.mellyappexternalapi.user.presentation.dto.request.ParticipateGroupRequest;
import cmc.mellyserver.mellyappexternalapi.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.mellyappexternalapi.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.common.enums.ScrapType;
import cmc.mellyserver.mellydomain.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellydomain.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellydomain.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellydomain.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellydomain.user.infrastructure.SurveyRecommendResponseDto;
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

	private final GroupService groupService;

	@GetMapping("/{userSeq}")
	public ResponseEntity<CommonResponse> getUserNickname(@PathVariable Long userSeq) {
		String nickname = userService.findNicknameByUserIdentifier(userSeq);
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, nickname));
	}

	@PostMapping("/survey")
	public ResponseEntity<CommonResponse> addSurvey(@AuthenticationPrincipal User user,
													@RequestBody SurveyRequest surveyRequest) {
		userService.createSurvey(UserAssembler.surveyRequestDto(Long.parseLong(user.getUsername()), surveyRequest));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@GetMapping("/survey")
	public ResponseEntity<CommonResponse> getSurvey(@AuthenticationPrincipal User user) {
		SurveyRecommendResponseDto surveyRecommendResponseDto = userService.getSurveyResult(
				Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(
				new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, surveyRecommendResponseDto));
	}

	@GetMapping("/profile")
	public ResponseEntity<CommonResponse> updateProfileFormData(@AuthenticationPrincipal User user) {
		ProfileUpdateFormResponseDto profileUpdateFormResponseDto = userService.getLoginUserProfileDataForUpdate(
				Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(
				new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, profileUpdateFormResponseDto));
	}

	@PutMapping("/profile")
	public ResponseEntity<CommonResponse> updateProfile(@AuthenticationPrincipal User user,
														ProfileUpdateRequest profileUpdateRequest) {
		userService.updateLoginUserProfile(
				UserAssembler.profileUpdateRequestDto(Long.parseLong(user.getUsername()), profileUpdateRequest));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@GetMapping("/memory")
	public ResponseEntity<CommonResponse> getUserMemory(@AuthenticationPrincipal User user,
														@PageableDefault(sort = "visitedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
														@RequestParam(required = false) GroupType groupType) {
		Slice<MemoryResponseDto> results = userService.findMemoriesLoginUserWrite(pageable,
				Long.parseLong(user.getUsername()), groupType);
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
				MemoryAssembler.memoryResponses(results)));
	}

	@GetMapping("/group")
	public ResponseEntity<CommonResponse> getUserGroup(@AuthenticationPrincipal User user) {
		List<GroupLoginUserParticipatedResponseDto> results = userService.findGroupListLoginUserParticiated(
				Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
				UserAssembler.groupLoginUserParticipatedResponses(results)));
	}

	@GetMapping("/group/{groupId}/memory")
	public ResponseEntity<CommonResponse> getMemoryBelongToMyGroup(Pageable pageable, @PathVariable Long groupId,
																   @RequestParam(required = false, name = "userId") Long userSeq) {
		Slice<MemoryResponseDto> results = userService.findMemoriesUsersBelongToMyGroupWrite(pageable, groupId,
				userSeq);
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
				MemoryAssembler.memoryResponses(results)));
	}

	@GetMapping("/place/scrap/count")
	public ResponseEntity<CommonResponse> getPlaceUserScrapCount(@AuthenticationPrincipal User user) {
		List<PlaceScrapCountResponseDto> results = placeScrapService.countByPlaceScrapType(
				Long.parseLong(user.getUsername()));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
				UserAssembler.placeScrapCountResponses(results)));
	}

	@GetMapping("/place/scrap")
	public ResponseEntity<CommonResponse> getPlaceUserScrap(@AuthenticationPrincipal User user, Pageable pageable,
															@RequestParam(required = false) ScrapType scrapType) {
		Slice<ScrapedPlaceResponseDto> results = placeScrapService.findScrapedPlace(pageable,
				Long.parseLong(user.getUsername()), scrapType);
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
				UserAssembler.scrapedPlaceResponses(results)));
	}

	@GetMapping("/volume")
	public ResponseEntity<CommonResponse> getUserImageVolume(@AuthenticationPrincipal User user) {
		Integer volume = userService.checkImageStorageVolumeLoginUserUse(user.getUsername());
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, volume));
	}

	@PostMapping("/group")
	public ResponseEntity<CommonResponse> participateToGroup(@AuthenticationPrincipal User user,
															 @RequestBody ParticipateGroupRequest participateGroupRequest) {
		groupService.participateToGroup(Long.parseLong(user.getUsername()), participateGroupRequest.getGroupId());
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}
}
