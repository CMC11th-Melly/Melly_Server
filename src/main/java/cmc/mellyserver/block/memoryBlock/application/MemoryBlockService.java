package cmc.mellyserver.block.memoryBlock.application;


import cmc.mellyserver.block.memoryBlock.domain.MemoryBlock;
import cmc.mellyserver.block.memoryBlock.domain.MemoryBlockRepository;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.report.memoryReport.domain.MemoryReport;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryBlockService {

    private final MemoryRepository memoryRepository;
    private final MemoryBlockRepository blockRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;

    @Transactional
    public void blockMemory(String uid, Long memoryId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });

        blockRepository.save(new MemoryBlock(user,memory));

    }


}
