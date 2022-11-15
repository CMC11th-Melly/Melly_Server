package cmc.mellyserver.comment.domain;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    Optional<CommentLike> findCommentLikeByCommentIdAndUserUserSeq(Long commentId, Long userSeq);

}
