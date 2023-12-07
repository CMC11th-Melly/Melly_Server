package cmc.mellyserver.dbcore.comment.commenlike;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

    List<CommentLike> findByUserId(Long userId);

}
