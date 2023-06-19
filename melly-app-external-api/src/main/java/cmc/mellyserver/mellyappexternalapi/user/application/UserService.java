package cmc.mellyserver.mellyappexternalapi.user.application;

import cmc.mellyserver.mellyappexternalapi.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellyappexternalapi.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.mellyappexternalapi.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellydomain.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellydomain.user.infrastructure.SurveyRecommendResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface UserService {

    List<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticiated(
            Long loginUserSeq);

    Slice<MemoryResponseDto> findMemoriesLoginUserWrite(Pageable pageable, Long loginUserSeq,
                                                        GroupType groupType);

    Slice<MemoryResponseDto> findMemoriesUsersBelongToMyGroupWrite(Pageable pageable, Long groupId,
                                                                   Long userSeq);

    Integer checkImageStorageVolumeLoginUserUse(String uuid);

    ProfileUpdateFormResponseDto getLoginUserProfileDataForUpdate(Long loginUserSeq);

    void updateLoginUserProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    String findNicknameByUserIdentifier(Long loginUserSeq);

    SurveyRecommendResponseDto getSurveyResult(Long loginUserSeq);

    void createSurvey(SurveyRequestDto surveyRequestDto);


}
