package cmc.mellyserver.block.commentBlock.presentation.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReportRequest {

    private Long commentId;
    private String content;
}
