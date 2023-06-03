package cmc.mellyserver.mellyapi.user.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;

import cmc.mellyserver.mellyapi.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellyapi.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.GroupLoginUserParticipatedResponse;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.PlaceScrapCountResponse;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.ScrapedPlaceResponse;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.ScrapedPlaceResponseDto;

public abstract class UserAssembler {

	private UserAssembler() {
	}

	public static List<GroupLoginUserParticipatedResponse> groupLoginUserParticipatedResponses(
		List<GroupLoginUserParticipatedResponseDto> groupLoginUserParticipatedResponseDtos) {
		return groupLoginUserParticipatedResponseDtos.stream().map(response ->
			GroupLoginUserParticipatedResponse.builder()
				.groupId(response.getGroupId())
				.groupType(response.getGroupType())
				.groupName(response.getGroupName())
				.groupIcon(response.getGroupIcon())
				.invitationLink(response.getInvitationLink())
				.build()
		).collect(Collectors.toList());
	}

	public static Slice<ScrapedPlaceResponse> scrapedPlaceResponses(
		Slice<ScrapedPlaceResponseDto> scrapedPlaceResponseDtos) {
		return scrapedPlaceResponseDtos.map(response -> ScrapedPlaceResponse.builder()
			.placeId(response.getPlaceId())
			.placeName(response.getPlaceName())
			.placeImage(response.getPlaceImage())
			.placeCategory(response.getPlaceCategory())
			.myMemoryCount(response.getMyMemoryCount())
			.otherMemoryCount(response.getOtherMemoryCount())
			.position(response.getPosition())
			.isScraped(response.getIsScraped())
			.recommendType(response.getRecommendType())
			.build());
	}

	public static List<PlaceScrapCountResponse> placeScrapCountResponses(
		List<PlaceScrapCountResponseDto> placeScrapCountResponseDtos) {
		return placeScrapCountResponseDtos.stream().map(response ->
				PlaceScrapCountResponse.builder()
					.scrapType(response.getScrapType())
					.scrapCount(response.getScrapCount()).build())
			.collect(Collectors.toList());
	}

	public static SurveyRequestDto surveyRequestDto(Long userSeq, SurveyRequest surveyRequest) {
		return SurveyRequestDto.builder()
			.userSeq(userSeq)
			.recommendGroup(surveyRequest.getRecommendGroup())
			.recommendPlace(surveyRequest.getRecommendPlace())
			.recommendActivity(surveyRequest.getRecommendActivity())
			.build();
	}

	public static ProfileUpdateRequestDto profileUpdateRequestDto(Long userSeq,
		ProfileUpdateRequest profileUpdateRequest) {
		return ProfileUpdateRequestDto.builder()
			.userSeq(userSeq)
			.profileImage(profileUpdateRequest.getProfileImage())
			.nickname(profileUpdateRequest.getNickname())
			.gender(profileUpdateRequest.getGender())
			.ageGroup(profileUpdateRequest.getAgeGroup())
			.deleteImage(profileUpdateRequest.isDeleteImage())
			.build();
	}
}
