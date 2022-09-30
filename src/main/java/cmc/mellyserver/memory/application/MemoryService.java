package cmc.mellyserver.memory.application;

import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.domain.service.MemoryDomainService;
import cmc.mellyserver.place.presentation.dto.PlaceInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoryService {

    private final MemoryRepository memoryRepository;
    private final MemoryDomainService memoryDomainService;
    public Memory createMemory(String userId, PlaceInfoRequest placeInfoRequest)
    {
        return memoryDomainService.createMemory(userId,
                placeInfoRequest.getLat(),
                placeInfoRequest.getLng(),
                placeInfoRequest.getTitle(),
                placeInfoRequest.getGroupType(),
                placeInfoRequest.getContent(),
                placeInfoRequest.getStar(),
                placeInfoRequest.getOpenType(),
                placeInfoRequest.getGroupId(),
                placeInfoRequest.getKeyword());
    }

}
