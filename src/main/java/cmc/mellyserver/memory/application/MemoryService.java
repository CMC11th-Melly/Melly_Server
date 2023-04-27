package cmc.mellyserver.memory.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.domain.repository.GroupRepository;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.memory.application.dto.response.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.memory.application.dto.response.MemoryForGroupResponse;
import cmc.mellyserver.memory.application.dto.response.MemoryUpdateFormResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryImage;
import cmc.mellyserver.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.memory.domain.dto.MemoryResponseDto;
import cmc.mellyserver.memory.domain.service.MemoryDomainService;
import cmc.mellyserver.memory.presentation.dto.common.MemoryAssembler;
import cmc.mellyserver.memory.presentation.dto.request.SearchMemoryByNameResponseDto;
import cmc.mellyserver.memory.presentation.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.place.presentation.dto.request.PlaceInfoRequest;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryService {


    private final MemoryDomainService memoryDomainService;
    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final MemoryQueryRepository memoryQueryRepository;
    private final MemoryRepository memoryRepository;
    private final S3FileLoader s3FileLoader;
    private final GroupRepository groupRepository;
    private final UserGroupQueryRepository userGroupQueryRepository;


    @Transactional
    public Memory createMemory(Long userSeq, List<MultipartFile> images, PlaceInfoRequest placeInfoRequest)
    {
        return memoryDomainService.createMemory(userSeq, placeInfoRequest.getLat(), placeInfoRequest.getLng(),
                placeInfoRequest.getTitle(), placeInfoRequest.getPlaceName(), placeInfoRequest.getPlaceCategory(),
                placeInfoRequest.getContent(), placeInfoRequest.getStar(), placeInfoRequest.getGroupId(),
                placeInfoRequest.getOpenType(), placeInfoRequest.getKeyword(), placeInfoRequest.getVisitedDate(), images);
    }


    public List<GroupListForSaveMemoryResponseDto> getGroupListForSaveMemory(Long userSeq)
    {
        return userGroupQueryRepository.getUserGroupListForMemoryEnroll(userSeq);
    }


    public List<SearchMemoryByNameResponseDto> searchPlaceByContainMemoryName(Long userSeq, String memoryName)
    {
        return memoryQueryRepository.searchPlaceByContainMemoryName(userSeq,memoryName);
    }


    public MemoryResponseDto getMemoryByMemoryId(Long userSeq, Long memoryId) {

          return memoryQueryRepository.getMemoryByMemoryId(userSeq,memoryId);
    }

    /**
     * 장소 상세 - 로그인한 유저가 이 장소에 작성한 메모리 조회
     * TODO : 찐막 검증 완료
     */
    public Slice<MemoryResponseDto> getUserMemoryInplace(Pageable pageable, Long userSeq, Long placeId, GroupType groupType)
    {
        return memoryQueryRepository.searchMemoryUserCreatedForPlace(pageable, userSeq, placeId, groupType);
    }

    /**
     * 장소 상세 - 로그인 유저가 아닌 다른 사람이 이 장소에 작성한 메모리 조회
     * TODO : 찐막 수정 완료
     */
    public Slice<MemoryResponseDto> getOtherMemoryInplace(Pageable pageable, Long userSeq, Long placeId, GroupType groupType)
    {
        return memoryQueryRepository.searchMemoryOtherCreate(pageable, userSeq, placeId, groupType);
    }


    /**
     * 마이페이지 - 내 그룹만 필터 조회 (최적화 완료,인덱스 필요)
     */
    public Slice<MemoryForGroupResponse> getMyGroupMemoryInplace(Pageable pageable,Long userSeq, Long placeId,GroupType groupType)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        Slice<Memory> myGroupMemory = memoryQueryRepository.getMyGroupMemory(pageable, user, placeId,groupType);
        return MemoryAssembler.memoryForGroupResponseSlice(myGroupMemory,user);
    }

   // TODO : 찐막 수정 완료
    @Transactional
    public void removeMemory(Long memoryId) {

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });
        memory.delete();
    }

    /**
     * 메모리 업데이트를 위한 폼 데이터 조회
     */
    public MemoryUpdateFormResponse getFormForUpdateMemory(Long userSeq, Long memoryId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);


//        List<GroupListForSaveMemoryResponseDto> collect = user.getGroupAndUsers().stream().map(ug ->
//                new GroupListForSaveMemoryResponseDto(ug.getGroup().getId(),
//                        ug.getGroup().getGroupName(),
//                        ug.getGroup().getGroupType())).collect(Collectors.toList());
//
//        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
//            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
//        });
//
//      return MemoryAssembler.memoryUpdateFormResponse(memory,collect);
    return null;
    }


    @Transactional
    public void updateMemory(String uid, Long memoryId, MemoryUpdateRequest memoryUpdateRequest, List<MultipartFile> images) {

        // 1. 업데이트할 메모리 찾기
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });

        // 3. 업데이트할 그룹 찾기
        UserGroup userGroup = groupRepository.findById(memoryUpdateRequest.getGroupId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });

        removeMemoryImages(memoryUpdateRequest, memory);

        List<String> multipartFileNames = s3FileLoader.getMultipartFileNames(uid, images);

        memory.updateMemory(memoryUpdateRequest.getTitle(),
                memoryUpdateRequest.getContent(),
                memoryUpdateRequest.getKeyword(),
                userGroup.getId(),
                userGroup.getGroupType(),
                userGroup.getGroupName(),
                memoryUpdateRequest.getOpenType(),
                memoryUpdateRequest.getVisitedDate(),
                memoryUpdateRequest.getStar());

        if(multipartFileNames != null)
        {
            memory.updateMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));
        }

    }


    private void removeMemoryImages(MemoryUpdateRequest memoryUpdateRequest, Memory memory) {
        if(!memoryUpdateRequest.getDeleteImageList().isEmpty())
        {
            for(Long deleteId : memoryUpdateRequest.getDeleteImageList())
            {
                memory.getMemoryImages().removeIf(memoryImage -> memoryImage.getId().equals(deleteId));
            }
        }
    }
}
