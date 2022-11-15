package cmc.mellyserver.memoryScrap.application;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import cmc.mellyserver.memoryScrap.domain.MemoryScrapDomainService;
import cmc.mellyserver.memoryScrap.presentation.dto.MemoryScrapRequest;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.placeScrap.presentation.dto.ScrapAssembler;
import cmc.mellyserver.user.domain.User;
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
