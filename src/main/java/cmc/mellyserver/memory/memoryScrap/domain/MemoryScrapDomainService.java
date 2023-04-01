package cmc.mellyserver.memory.memoryScrap.domain;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import cmc.mellyserver.place.placeScrap.presentation.dto.ScrapAssembler;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemoryScrapDomainService {

    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final MemoryScrapRepository memoryScrapRepository;
    private final MemoryRepository memoryRepository;
    private final MemoryQueryRepository memoryQueryRepository;

    public Slice<ScrapedMemoryResponseDto> getScrapMemory(Pageable pageable, String uid, GroupType groupType)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        Slice<Memory> result = memoryQueryRepository.getScrapedMemory(pageable, user,groupType);
        return result.map(m -> ScrapAssembler.scrapedMemoryResponseDto(m));
    }

    @Transactional
    public void createScrap(String uid, Long memoryId)
    {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });
        checkDuplicatedScrap(user.getUserSeq(),memory);

        memoryScrapRepository.save(MemoryScrap.createScrap(user,memory));

    }


    @Transactional
    public void removeScrap(String uid, Long memoryId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });
        checkExistScrap(user.getUserSeq(),memory);

        memoryScrapRepository.deleteByUserUserSeqAndMemory(user.getUserSeq(),memory);
    }


    private void checkDuplicatedScrap(Long userSeq, Memory memory) {
        memoryScrapRepository.findMemoryScrapByUserUserSeqAndMemory(userSeq,memory)
                .ifPresent(x -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_SCRAP);});
    }


    private void checkExistScrap(Long userSeq, Memory memory) {
        memoryScrapRepository.findMemoryScrapByUserUserSeqAndMemory(userSeq,memory)
                .orElseThrow(() -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NOT_EXIST_SCRAP);});
    }
}
