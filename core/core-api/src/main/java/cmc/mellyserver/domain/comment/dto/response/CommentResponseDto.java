package cmc.mellyserver.domain.comment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CommentResponseDto {
    private int commentCount;

    private List<CommentDto> comments;

    @Builder
    public CommentResponseDto(int commentCount, List<CommentDto> comments) {
        this.commentCount = commentCount;
        this.comments = comments;
    }
}
