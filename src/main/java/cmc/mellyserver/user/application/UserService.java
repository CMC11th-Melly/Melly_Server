package cmc.mellyserver.user.application;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponseDto;
import cmc.mellyserver.user.application.dto.SurveyRecommendResponseDto;
import cmc.mellyserver.user.presentation.dto.request.ProfileUpdateRequestDto;
import cmc.mellyserver.user.presentation.dto.request.SurveyRequestDto;
import cmc.mellyserver.user.presentation.dto.response.GroupLoginUserParticipatedResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface UserService {

    List<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticiated(Long loginUserSeq);

    Slice<MemoryResponseDto> findMemoriesLoginUserWrite(Pageable pageable, Long loginUserSeq, GroupType groupType);

    Slice<MemoryResponseDto> findMemoriesUsersBelongToMyGroupWrite(Pageable pageable,Long groupId,Long userSeq);

    Integer checkImageStorageVolumeLoginUserUse(String uuid);

    ProfileUpdateFormResponseDto getLoginUserProfileDataForUpdate(Long loginUserSeq);

    void updateLoginUserProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    String findNicknameByUserIdentifier(Long loginUserSeq);

    SurveyRecommendResponseDto getSurveyResult(Long loginUserSeq);

    void createSurvey(SurveyRequestDto surveyRequestDto);

    void participateToGroup(Long loginUserSeq, Long groupId);
}
