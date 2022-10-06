package cmc.mellyserver.memory.application;

import cmc.mellyserver.common.util.AuthenticatedUserChecker;
import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.domain.service.MemoryDomainService;
import cmc.mellyserver.place.presentation.dto.PlaceInfoRequest;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Memory createMemory(String userId, PlaceInfoRequest placeInfoRequest,List<String> keyword)
    {
        return memoryDomainService.createMemory(userId,
                placeInfoRequest.getLat(),
                placeInfoRequest.getLng(),
                placeInfoRequest.getTitle(),
                placeInfoRequest.getContent(),
                placeInfoRequest.getStar(),
                placeInfoRequest.getGroupId(),
                placeInfoRequest.getGroupType(),
                keyword,
                placeInfoRequest.getImages());
    }

    /*
    TODO : 그룹의 ID , 그룹 이름, 그룹 타입 반환
     */
    public List<MemoryFormGroupResponse> getUserGroup(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return user.getGroupAndUsers().stream().map(ug ->
                new MemoryFormGroupResponse(ug.getGroup().getId(),
                        ug.getGroup().getGroupName(),
                        ug.getGroup().getGroupType())).collect(Collectors.toList());

    }

    public void search(String uid,String title)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        List<Memory> result = memoryQueryRepository.searchMember(user.getUserSeq(), title);

    }


}
