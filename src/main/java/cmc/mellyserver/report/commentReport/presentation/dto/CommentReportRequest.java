package cmc.mellyserver.report.commentReport.presentation.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReportRequest {

    private Long commentId;
}
