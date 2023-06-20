package cmc.mellyserver.mellydomain.comment.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cmc.mellyserver.mellydomain.comment.domain.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
	Optional<CommentLike> findCommentLikeByCommentIdAndUserId(Long commentId, Long userId);

}
