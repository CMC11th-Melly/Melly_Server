package cmc.mellyserver.comment.domain.repository;


import cmc.mellyserver.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    Optional<CommentLike> findCommentLikeByCommentIdAndUserId(Long commentId, Long userId);

}
