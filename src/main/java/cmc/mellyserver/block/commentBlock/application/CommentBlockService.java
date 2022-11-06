package cmc.mellyserver.block.commentBlock.application;

import cmc.mellyserver.block.commentBlock.domain.CommentBlock;
import cmc.mellyserver.block.commentBlock.domain.CommentBlockRepository;
import cmc.mellyserver.comment.domain.Comment;
import cmc.mellyserver.comment.domain.CommentRepository;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.report.commentReport.domain.CommentReport;
import cmc.mellyserver.report.commentReport.domain.CommentReportRepository;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentBlockService {

    private final CommentBlockRepository blockRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final CommentRepository commentRepository;

    public void commentBlock(String uid, Long commentId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
        });
        blockRepository.save(new CommentBlock(user,comment));
        comment.setIsReported(true);
    }
}
