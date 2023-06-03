package cmc.mellyserver.mellyapi.user.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import cmc.mellyserver.mellyapi.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellyapi.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.mellyapi.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.user.infrastructure.SurveyRecommendResponseDto;

public interface UserService {

	List<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticiated(Long loginUserSeq);

	Slice<MemoryResponseDto> findMemoriesLoginUserWrite(Pageable pageable, Long loginUserSeq, GroupType groupType);

	Slice<MemoryResponseDto> findMemoriesUsersBelongToMyGroupWrite(Pageable pageable, Long groupId, Long userSeq);

	Integer checkImageStorageVolumeLoginUserUse(String uuid);

	ProfileUpdateFormResponseDto getLoginUserProfileDataForUpdate(Long loginUserSeq);

	void updateLoginUserProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

	String findNicknameByUserIdentifier(Long loginUserSeq);

	SurveyRecommendResponseDto getSurveyResult(Long loginUserSeq);

	void createSurvey(SurveyRequestDto surveyRequestDto);

	void participateToGroup(Long loginUserSeq, Long groupId);
}
