package cmc.mellyserver.mellyapi.memory.application.impl;

import cmc.mellyserver.mellyapi.common.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellyapi.common.aws.FileUploader;
import cmc.mellyserver.mellyapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyapi.memory.application.MemoryService;
import cmc.mellyserver.mellyapi.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellyapi.memory.application.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.mellyapi.memory.application.dto.response.MemoryImageDto;
import cmc.mellyserver.mellyapi.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.common.enums.OpenType;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.MemoryImage;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.memory.domain.vo.GroupInfo;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.user.domain.User;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryServiceImpl implements MemoryService {

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final MemoryRepository memoryRepository;

    private final MemoryQueryRepository memoryQueryRepository;

    private final PlaceRepository placeRepository;

    private final FileUploader fileUploader;

    private final GroupRepository groupRepository;

    private final UserGroupQueryRepository userGroupQueryRepository;

    private final GroupAndUserRepository groupAndUserRepository;


    @Cacheable(value = "findGroups", key = "#userSeq")
    @Override
    public List<GroupListForSaveMemoryResponseDto> findGroupListLoginUserParticipate(Long userSeq) {
        return userGroupQueryRepository.getUserGroupListForMemoryEnroll(userSeq);
    }


    @Cacheable(value = "memory", key = "'memoryId'")
    @Override
    public MemoryResponseDto findMemoryByMemoryId(Long userSeq, Long memoryId) {
        return memoryQueryRepository.getMemoryByMemoryId(userSeq, memoryId);
    }


    @Override
    public Slice<MemoryResponseDto> findLoginUserWriteMemoryBelongToPlace(Pageable pageable,
            Long userSeq, Long placeId, GroupType groupType) {
        return memoryQueryRepository.searchMemoryUserCreatedForPlace(pageable, userSeq, placeId,
                groupType);
    }


    @Override
    public Slice<MemoryResponseDto> findOtherUserWriteMemoryBelongToPlace(Pageable pageable,
            Long userSeq, Long placeId, GroupType groupType) {
        return memoryQueryRepository.searchMemoryOtherCreate(pageable, userSeq, placeId, groupType);
    }

    @Override
    public Slice<MemoryResponseDto> findMyGroupMemberWriteMemoryBelongToPlace(Pageable pageable,
            Long userSeq,
            Long placeId, GroupType groupType) {
        return memoryQueryRepository.getMyGroupMemory(pageable, userSeq, placeId, groupType);
    }


    @Override
    public MemoryUpdateFormResponseDto findMemoryUpdateFormData(Long userSeq, Long memoryId) {

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });

        List<UserGroup> userGroupLoginUserAssociated = groupAndUserRepository.findUserGroupLoginUserAssociated(
                userSeq);

        return new MemoryUpdateFormResponseDto(
                memory.getMemoryImages()
                        .stream()
                        .map(mi -> new MemoryImageDto(mi.getId(), mi.getImagePath()))
                        .collect(Collectors.toList()),
                memory.getTitle(),
                memory.getContent(),
                userGroupLoginUserAssociated.stream()
                        .map(ug -> new GroupListForSaveMemoryResponseDto(ug.getId(),
                                ug.getGroupName(), ug.getGroupType()))
                        .collect(Collectors.toList()),
                memory.getStars(),
                memory.getKeyword()
        );
    }

    @Override
    public List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryName(Long userSeq,
            String memoryName) {
        return memoryQueryRepository.searchPlaceByContainMemoryName(userSeq, memoryName);
    }


    @Override
    @Transactional
    public Memory createMemory(CreateMemoryRequestDto createMemoryRequestDto) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(
                createMemoryRequestDto.getUserSeq());

        List<String> multipartFileNames = fileUploader.getMultipartFileNames(user.getUserId(),
                createMemoryRequestDto.getMultipartFiles());

        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(
                new Position(createMemoryRequestDto.getLat(),
                        createMemoryRequestDto.getLng())); // 해당 좌표를 가진 장소 정보가 기존에 존재하는지 체크

        // 만약 아직 장소가 없다면?
        if (placeOpt.isEmpty()) {
            Place savePlace = placeRepository.save(Place.builder()
                    .position(new Position(createMemoryRequestDto.getLat(),
                            createMemoryRequestDto.getLng()))
                    .placeCategory(createMemoryRequestDto.getPlaceCategory())
                    .placeName(createMemoryRequestDto.getPlaceName())
                    .build());

            Memory memory;

            // 1. 만약 그룹이 없다면?
            if (createMemoryRequestDto.getGroupId() == null
                    || createMemoryRequestDto.getGroupId() == -1L) {
                memory = Memory.builder()
                        .title(createMemoryRequestDto.getTitle())
                        .content(createMemoryRequestDto.getContent())
                        .openType(OpenType.ALL)
                        .groupInfo(new GroupInfo("그룹 미설정", GroupType.ALL, -1L))
                        .openType(createMemoryRequestDto.getOpenType())
                        .stars(createMemoryRequestDto.getStar())
                        .visitedDate(createMemoryRequestDto.getVisitedDate())
                        .build();
            } else {
                UserGroup userGroup = groupRepository.findById(createMemoryRequestDto.getGroupId())
                        .orElseThrow(() -> {
                            throw new GlobalBadRequestException(
                                    ExceptionCodeAndDetails.NO_SUCH_GROUP);
                        });
                memory = Memory.builder()
                        .title(createMemoryRequestDto.getTitle())
                        .content(createMemoryRequestDto.getContent())
                        .groupInfo(new GroupInfo(userGroup.getGroupName(), userGroup.getGroupType(),
                                createMemoryRequestDto.getGroupId()))
                        .visitedDate(createMemoryRequestDto.getVisitedDate())
                        .openType(createMemoryRequestDto.getOpenType())
                        .stars(createMemoryRequestDto.getStar())
                        .build();
            }

            // user 세팅
            memory.setUserId(createMemoryRequestDto.getUserSeq());
            // memoryImage 세팅
            memory.setMemoryImages(
                    multipartFileNames.stream().map(m -> new MemoryImage(m))
                            .collect(Collectors.toList()));
            // 연관된 장소 세팅
            memory.setPlaceId(savePlace.getId());
            // 키워드 등록
            memory.setKeyword(createMemoryRequestDto.getKeyword());
            // 트렌드 분석에 사용되는 장소 이름 추가
            return memoryRepository.save(memory);
        } else {

            Memory memory;

            if (createMemoryRequestDto.getGroupId() == null
                    || createMemoryRequestDto.getGroupId() == -1L) {
                memory = Memory.builder()
                        .title(createMemoryRequestDto.getTitle())
                        .content(createMemoryRequestDto.getContent())
                        .groupInfo(new GroupInfo("그룹 미설정", GroupType.ALL, -1L))
                        .openType(createMemoryRequestDto.getOpenType())
                        .stars(createMemoryRequestDto.getStar())
                        .visitedDate(createMemoryRequestDto.getVisitedDate())
                        .build();
            } else {
                UserGroup userGroup = groupRepository.findById(createMemoryRequestDto.getGroupId())
                        .orElseThrow(() -> {
                            throw new GlobalBadRequestException(
                                    ExceptionCodeAndDetails.NO_SUCH_GROUP);
                        });
                memory = Memory.builder()
                        .title(createMemoryRequestDto.getTitle())
                        .content(createMemoryRequestDto.getContent())
                        .groupInfo(new GroupInfo(userGroup.getGroupName(), userGroup.getGroupType(),
                                createMemoryRequestDto.getGroupId()))
                        .openType(createMemoryRequestDto.getOpenType())
                        .stars(createMemoryRequestDto.getStar())
                        .visitedDate(createMemoryRequestDto.getVisitedDate())
                        .build();
            }

            memory.setUserId(createMemoryRequestDto.getUserSeq());

            if (multipartFileNames != null) {
                memory.setMemoryImages(
                        multipartFileNames.stream().map(m -> new MemoryImage(m))
                                .collect(Collectors.toList()));
            }

            memory.setPlaceId(placeOpt.get().getId());
            memory.setKeyword(createMemoryRequestDto.getKeyword());
            return memoryRepository.save(memory);
        }
    }


    @CacheEvict(value = "memory")
    @Override
    @Transactional
    public void removeMemory(Long userSeq, Long memoryId) {

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });
        memory.delete();
    }


    @CacheEvict(value = "memory", key = "{#updateMemoryRequestDto.userSeq,#updateMemoryRequestDto.memoryId}")
    @Override
    @Transactional
    public void updateMemory(UpdateMemoryRequestDto updateMemoryRequestDto) {

        Memory memory = memoryRepository.findById(updateMemoryRequestDto.getMemoryId())
                .orElseThrow(() -> {
                    throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
                });

        UserGroup userGroup = groupRepository.findById(updateMemoryRequestDto.getGroupId())
                .orElseThrow(() -> {
                    throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
                });

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(
                updateMemoryRequestDto.getUserSeq());

        removeMemoryImages(updateMemoryRequestDto, memory);

        List<String> multipartFileNames = fileUploader.getMultipartFileNames(user.getUserId(),
                updateMemoryRequestDto.getImages());

        memory.updateMemory(updateMemoryRequestDto.getTitle(), updateMemoryRequestDto.getContent(),
                updateMemoryRequestDto.getKeyword(), userGroup.getId(),
                userGroup.getGroupType(), userGroup.getGroupName(),
                updateMemoryRequestDto.getOpenType(),
                updateMemoryRequestDto.getVisitedDate(), updateMemoryRequestDto.getStar());

        if (!Objects.isNull(multipartFileNames)) {
            memory.setMemoryImages(
                    multipartFileNames.stream().map(m -> new MemoryImage(m))
                            .collect(Collectors.toList()));
        }

    }

    private void removeMemoryImages(UpdateMemoryRequestDto updateMemoryRequestDto, Memory memory) {
        if (!Objects.isNull(updateMemoryRequestDto.getDeleteImageList())
                && !updateMemoryRequestDto.getDeleteImageList()
                .isEmpty()) {
            for (Long deleteId : updateMemoryRequestDto.getDeleteImageList()) {
                memory.getMemoryImages()
                        .removeIf(memoryImage -> memoryImage.getId().equals(deleteId));
            }
        }
    }
}
