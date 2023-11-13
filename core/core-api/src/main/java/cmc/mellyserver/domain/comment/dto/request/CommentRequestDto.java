package cmc.mellyserver.domain.comment.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentRequestDto {

    private Long userId;

    private Long rootId; // 루트 댓글 ID

    private Long mentionId; // 댓글을 단 ID

    private String content;

    private Long memoryId;

    @Builder
    public CommentRequestDto(Long userId, Long mentionId, String content, Long memoryId, Long rootId) {

        this.userId = userId;
        this.mentionId = mentionId;
        this.content = content;
        this.memoryId = memoryId;
        this.rootId = rootId;
    }

}
