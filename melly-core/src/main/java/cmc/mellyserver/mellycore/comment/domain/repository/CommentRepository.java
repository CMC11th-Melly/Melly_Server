package cmc.mellyserver.mellycore.comment.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cmc.mellyserver.mellycore.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("select c from Comment c left join fetch c.parent where c.id = :id")
	Optional<Comment> findCommentByIdWithParent(@Param("id") Long id);
}
