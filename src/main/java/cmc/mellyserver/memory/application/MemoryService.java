package cmc.mellyserver.memory.application;

import cmc.mellyserver.common.enums.OpenType;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.group.domain.repository.GroupRepository;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.memory.application.dto.MemoryAssembler;
import cmc.mellyserver.memory.application.dto.response.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.memory.application.dto.response.MemoryUpdateFormResponse;
import cmc.mellyserver.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.memory.domain.vo.GroupInfo;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryImage;
import cmc.mellyserver.memory.infrastructure.data.dto.MemoryResponseDto;
import cmc.mellyserver.memory.presentation.dto.request.SearchMemoryByNameResponseDto;
import cmc.mellyserver.memory.presentation.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
import cmc.mellyserver.place.presentation.dto.request.PlaceInfoRequest;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryService {

    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final MemoryRepository memoryRepository;
    private final PlaceRepository placeRepository;
    private final S3FileLoader s3FileLoader;
    private final GroupRepository groupRepository;
    private final UserGroupQueryRepository userGroupQueryRepository;
    private final GroupAndUserRepository groupAndUserRepository;


    @Transactional
    public Memory createMemory(Long userSeq, List<MultipartFile> images, PlaceInfoRequest placeInfoRequest)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        List<String> multipartFileNames = s3FileLoader.getMultipartFileNames(user.getUserId(),images);

        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(placeInfoRequest.getLat(),placeInfoRequest.getLng())); // 해당 좌표를 가진 장소 정보가 기존에 존재하는지 체크

        // 만약 아직 장소가 없다면?
        if(placeOpt.isEmpty())
        {
            Place savePlace = placeRepository.save(Place.builder().position(new Position(placeInfoRequest.getLat(),placeInfoRequest.getLng())).placeCategory(placeInfoRequest.getPlaceCategory()).placeName(placeInfoRequest.getPlaceName()).build());

            Memory memory;

            // 1. 만약 그룹이 없다면?
            if(placeInfoRequest.getGroupId() == null || placeInfoRequest.getGroupId() == -1L)
            {
                memory  = Memory.builder().title(placeInfoRequest.getTitle()).content(placeInfoRequest.getContent()).openType(OpenType.ALL).groupInfo(new GroupInfo("그룹 미설정",GroupType.ALL,-1L)).openType(placeInfoRequest.getOpenType()).stars(placeInfoRequest.getStar()).visitedDate(placeInfoRequest.getVisitedDate()).build();
            }
            else{
                UserGroup userGroup = groupRepository.findById(placeInfoRequest.getGroupId()).orElseThrow(() -> {
                    throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
                });
                memory = Memory.builder().title(placeInfoRequest.getTitle()).content(placeInfoRequest.getContent()).groupInfo(new GroupInfo(userGroup.getGroupName(),userGroup.getGroupType(),placeInfoRequest.getGroupId())).visitedDate(placeInfoRequest.getVisitedDate()).openType(placeInfoRequest.getOpenType()).stars(placeInfoRequest.getStar()).build();
            }

            // user 세팅
            memory.setUserId(userSeq);
            // memoryImage 세팅
            memory.setMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));
            // 연관된 장소 세팅
            memory.setPlaceId(savePlace.getId());
            // 키워드 등록
            memory.setKeyword(placeInfoRequest.getKeyword());
            // 트렌드 분석에 사용되는 장소 이름 추가
            return memoryRepository.createMemory(memory);
        }
        else{

            Memory memory;

            if(placeInfoRequest.getGroupId() == null || placeInfoRequest.getGroupId() == -1L)
            {
                memory =  Memory.builder().title(placeInfoRequest.getTitle()).content(placeInfoRequest.getContent()).groupInfo(new GroupInfo("그룹 미설정",GroupType.ALL,-1L)).openType(placeInfoRequest.getOpenType()).stars(placeInfoRequest.getStar()).visitedDate(placeInfoRequest.getVisitedDate()).build();
            }
            else{
                UserGroup userGroup = groupRepository.findById(placeInfoRequest.getGroupId()).orElseThrow(() -> {
                    throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
                });
                memory = Memory.builder().title(placeInfoRequest.getTitle()).content(placeInfoRequest.getContent()).groupInfo(new GroupInfo(userGroup.getGroupName(),userGroup.getGroupType(),placeInfoRequest.getGroupId())).openType(placeInfoRequest.getOpenType()).stars(placeInfoRequest.getStar()).visitedDate(placeInfoRequest.getVisitedDate()).build();
            }

            memory.setUserId(userSeq);

            if(multipartFileNames != null)
            {
                memory.setMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));
            }

            memory.setPlaceId(placeOpt.get().getId());
            memory.setKeyword(placeInfoRequest.getKeyword());
            return memoryRepository.createMemory(memory);
        }
    }


    // ok
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



    public Slice<MemoryResponseDto> getUserMemoryInplace(Pageable pageable, Long userSeq, Long placeId, GroupType groupType)
    {
        return memoryQueryRepository.searchMemoryUserCreatedForPlace(pageable, userSeq, placeId, groupType);
    }



    public Slice<MemoryResponseDto> getOtherMemoryInplace(Pageable pageable, Long userSeq, Long placeId, GroupType groupType)
    {
        return memoryQueryRepository.searchMemoryOtherCreate(pageable, userSeq, placeId, groupType);
    }



    public Slice<MemoryResponseDto> getMyGroupMemoryInplace(Pageable pageable,Long userSeq, Long placeId,GroupType groupType)
    {
        return memoryQueryRepository.getMyGroupMemory(pageable, userSeq, placeId,groupType);
    }



    @Transactional
    public void removeMemory(Long memoryId) {

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });
        memory.delete();
    }



    public MemoryUpdateFormResponse getFormForUpdateMemory(Long userSeq, Long memoryId) {

        Memory memory = memoryRepository.findMemoryById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });

        List<UserGroup> userGroupLoginUserAssociated = groupAndUserRepository.findUserGroupLoginUserAssociated(userSeq);

        return MemoryAssembler.memoryUpdateFormResponse(memory,userGroupLoginUserAssociated);
    }



    @Transactional
    public void updateMemory(String uid, Long memoryId, MemoryUpdateRequest memoryUpdateRequest, List<MultipartFile> images) {


        Memory memory = memoryRepository.findMemoryById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });

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
