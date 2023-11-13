package cmc.mellyserver.dbcore.comment.comment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("select c from Comment c left join fetch c.root where c.id = :id")
	Optional<Comment> findCommentByIdWithRoot(@Param("id") Long id);

}
