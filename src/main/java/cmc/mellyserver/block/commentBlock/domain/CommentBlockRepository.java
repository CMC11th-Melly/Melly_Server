package cmc.mellyserver.block.commentBlock.domain;

import cmc.mellyserver.report.commentReport.domain.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentBlockRepository extends JpaRepository<CommentBlock,Long> {
}
