package cmc.mellyserver.memory.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.application.dto.MemoryForGroupResponse;
import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import cmc.mellyserver.memory.application.dto.MemoryImageDto;
import cmc.mellyserver.memory.application.dto.MemoryUpdateFormResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.memory.presentation.dto.*;
import cmc.mellyserver.memory.domain.service.MemoryDomainService;
import cmc.mellyserver.place.presentation.dto.PlaceInfoRequest;
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


    public Slice<Memory> getUserMemory(Long lastMemoryId, Pageable pageable, String uid, Long placeId, GetUserMemoryCond getUserMemoryCond)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        return memoryQueryRepository.searchMemoryUserCreate(lastMemoryId,pageable,user.getUserSeq(),
                                                            placeId,
                                                            getUserMemoryCond.getKeyword(),
                                                            getUserMemoryCond.getGroupType(),
                                                            getUserMemoryCond.getVisitedDate());
    }

    public Slice<Memory> getOtherMemory(Long lastId, Pageable pageable, String uid,Long placeId, GetOtherMemoryCond getOtherMemoryCond) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return memoryQueryRepository.searchMemoryOtherCreate(lastId, pageable,user.getUserSeq(),
                placeId,
                getOtherMemoryCond.getGroupType(),
                getOtherMemoryCond.getKeyword(),
                getOtherMemoryCond.getVisitedDate());
    }

    public Slice<Memory> getOtherMemorySameGroupCreated(Long lastId, Pageable pageable, String uid,Long placeId, GetOtherMemoryCond getOtherMemoryCond) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        return memoryQueryRepository.searchMemoryOtherCreate(lastId, pageable,user.getUserSeq(),
                placeId,
                getOtherMemoryCond.getGroupType(),
                getOtherMemoryCond.getKeyword(),
                getOtherMemoryCond.getVisitedDate());
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

    public Slice<MemoryForGroupResponse> getMyGroupMemory(Long lastId, Pageable pageable, String uid, Long placeId) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        Slice<Memory> myGroupMemory = memoryQueryRepository.getMyGroupMemory(lastId, pageable, user, placeId);

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
}
