package cmc.mellyserver.report.memoryReport.application;


import cmc.mellyserver.comment.domain.Comment;
import cmc.mellyserver.comment.domain.CommentRepository;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.report.memoryReport.domain.MemoryReport;
import cmc.mellyserver.report.memoryReport.domain.MemoryReportRepository;
import cmc.mellyserver.report.memoryReport.domain.ReportType;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final MemoryRepository memoryRepository;
    private final MemoryReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;

    @Transactional
    public void reportMemory(String uid, Long memoryId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });

        reportRepository.save(new MemoryReport(user,memory));
        memory.isReported(true);

    }

//    @Transactional
//    public void reportComment(String uid, Long commentId) {
//
//        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
//        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
//            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
//        });
//        reportRepository.save(new Report(user,, ReportType.MEMORY));
//        memory.isReported(true);
//    }
}
