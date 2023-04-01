package cmc.mellyserver.memory.memoryScrap.application;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.memoryScrap.domain.MemoryScrapDomainService;
import cmc.mellyserver.memory.memoryScrap.presentation.dto.MemoryScrapRequest;
import cmc.mellyserver.memory.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoryScrapService {

    private final MemoryScrapDomainService memoryScrapDomainService;

    public Slice<ScrapedMemoryResponseDto> getScrapedMemory(Pageable pageable, String uid, GroupType groupType) {
        return memoryScrapDomainService.getScrapMemory(pageable,uid,groupType);
    }

    @Transactional
    public void createScrap(String uid, MemoryScrapRequest memoryScrapRequest) {

        memoryScrapDomainService.createScrap(uid, memoryScrapRequest.getMemoryId());
    }

    @Transactional
    public void removeScrap(String uid, Long memoryId) {
        memoryScrapDomainService.removeScrap(uid,memoryId);
    }



}
