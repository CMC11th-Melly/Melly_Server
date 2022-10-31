package cmc.mellyserver.memory.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.application.dto.MemoryForGroupResponse;
import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import cmc.mellyserver.memory.application.dto.MemoryImageDto;
import cmc.mellyserver.memory.application.dto.MemoryUpdateFormResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryImage;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.memory.presentation.dto.*;
import cmc.mellyserver.memory.domain.service.MemoryDomainService;
import cmc.mellyserver.place.presentation.dto.PlaceInfoRequest;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.geolatte.geom.M;
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

    @Transactional
    public Memory createMemory(String uid, List<MultipartFile> images, PlaceInfoRequest placeInfoRequest)
    {
        return memoryDomainService.createMemory(uid,
                placeInfoRequest.getLat(),
                placeInfoRequest.getLng(),
                placeInfoRequest.getTitle(),
                placeInfoRequest.getPlaceName(),
                placeInfoRequest.getPlaceCategory(),
                placeInfoRequest.getContent(),
                placeInfoRequest.getStar(),
                placeInfoRequest.getGroupId(),
                placeInfoRequest.getOpenType(),
                placeInfoRequest.getKeyword(),
                placeInfoRequest.getVisitedDate(),
                images);
    }


    public List<MemoryFormGroupResponse> getUserGroupForMemoryForm(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        return user.getGroupAndUsers().stream().map(ug ->
                new MemoryFormGroupResponse(ug.getGroup().getId(),
                        ug.getGroup().getGroupName(),
                        ug.getGroup().getGroupType())).collect(Collectors.toList());
    }


    public List<MemorySearchDto> searchPlaceByMemoryTitle(String uid, String memoryName)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return memoryQueryRepository.searchMemoryName(user.getUserSeq(),memoryName);
    }


    public Slice<Memory> getUserMemory(Pageable pageable, String uid, Long placeId, GroupType groupType)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        return memoryQueryRepository.searchMemoryUserCreate(pageable,user.getUserSeq(), placeId, groupType);
    }

    public Slice<Memory> getOtherMemory(Pageable pageable, String uid,Long placeId, GroupType groupType) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return memoryQueryRepository.searchMemoryOtherCreate(pageable,user.getUserSeq(),
                placeId,groupType);
    }


    @Transactional
    public void removeMemory(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });
        memoryRepository.delete(memory);
    }


    public MemoryUpdateFormResponse getFormForUpdateMemory(String uid, Long memoryId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        List<MemoryFormGroupResponse> collect = user.getGroupAndUsers().stream().map(ug ->
                new MemoryFormGroupResponse(ug.getGroup().getId(),
                        ug.getGroup().getGroupName(),
                        ug.getGroup().getGroupType())).collect(Collectors.toList());

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });

      return MemoryAssembler.memoryUpdateFormResponse(memory,collect);

    }

    public Slice<MemoryForGroupResponse> getMyGroupMemory(Pageable pageable, String uid, Long placeId,GroupType groupType) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        Slice<Memory> myGroupMemory = memoryQueryRepository.getMyGroupMemory(pageable, user, placeId,groupType);

        return myGroupMemory
                .map(memory -> new MemoryForGroupResponse(memory.getPlace().getId(),
                                memory.getPlace().getPlaceName(),
                                memory.getId(),
                                memory.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(), mi.getImagePath()))
                                        .collect(Collectors.toList()),
                                memory.getTitle(),
                                memory.getContent(),
                                memory.getGroupInfo().getGroupType(),
                                memory.getGroupInfo().getGroupName(),
                                memory.getStars(),
                                memory.getKeyword(),
                                memory.getVisitedDate()));


    }

    @Transactional
    public void updateMemory(String uid, Long memoryId, MemoryUpdateRequest memoryUpdateRequest,List<MultipartFile> images) {

        // 1. 업데이트할 메모리 찾기
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });

//        // 2. 삭제할 이미지 목록 조회
//        List<Long> results = memory.getMemoryImages().stream().filter(mi ->
//                memoryUpdateRequest.getDeleteImageList().contains(mi.getId())
//        ).map(fmi -> fmi.getId()).collect(Collectors.toList());

        // 3. 업데이트할 그룹 찾기
        UserGroup userGroup = groupRepository.findById(memoryUpdateRequest.getGroupId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });

        // TODO : 만약에 삭제 안 하면 빈 배열로 넣는다는거를 공지!
        if(!memoryUpdateRequest.getDeleteImageList().isEmpty())
        {
            System.out.println("메모리 사진을 삭제할꺼야");
            for(Long deleteId : memoryUpdateRequest.getDeleteImageList())
            {
                memory.getMemoryImages().removeIf(memoryImage -> memoryImage.getId().equals(deleteId));
            }
        }

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
}
