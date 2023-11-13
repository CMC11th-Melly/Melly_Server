package cmc.mellyserver.dbcore.comment.commenlike;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

    List<CommentLike> findByUserId(Long userId);

}
