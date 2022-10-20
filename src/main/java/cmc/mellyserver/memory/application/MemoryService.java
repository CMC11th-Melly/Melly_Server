package cmc.mellyserver.memory.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.memory.presentation.dto.MemorySearchDto;
import cmc.mellyserver.memory.domain.service.MemoryDomainService;
import cmc.mellyserver.memory.presentation.dto.GetOtherMemoryCond;
import cmc.mellyserver.memory.presentation.dto.GetUserMemoryCond;
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
                getOtherMemoryCond.getKeyword(),
                getOtherMemoryCond.getVisitedDate());
    }


}
