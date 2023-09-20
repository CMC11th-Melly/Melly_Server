package cmc.mellyserver.mellycore.comment.domain.repository;

import cmc.mellyserver.mellycore.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("select c from Comment c left join fetch c.parent where c.id = :id")
	Optional<Comment> findCommentByIdWithParent(@Param("id") Long id);


}
