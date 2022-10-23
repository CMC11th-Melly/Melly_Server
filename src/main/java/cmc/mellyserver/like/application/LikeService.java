package cmc.mellyserver.like.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.like.domain.Like;
import cmc.mellyserver.like.domain.LikeRepository;
import cmc.mellyserver.like.domain.LikeType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemoryRepository memoryRepository;

    @Transactional
    public void addLike(Long memoryId, LikeType likeType){

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });

        Like like = Like.createLike(memory, likeType);
        likeRepository.save(like);

    }
}
