package cmc.mellyserver.report.commentReport.application;

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
public class CommentReportService {

    private final CommentReportRepository reportRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final CommentRepository commentRepository;

    public void createReport(String uid, Long commentId,String content) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
        });
        reportRepository.save(new CommentReport(user,comment,content));
        comment.setIsReported(true);
    }
}
