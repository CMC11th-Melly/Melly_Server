package cmc.mellyserver.mellycore.comment.application.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentRequestDto {

    private Long userId;

    private String content;

    private Long memoryId;

    private Long parentId;


    @Builder
    public CommentRequestDto(Long userId, String content, Long memoryId, Long parentId) {

        this.userId = userId;
        this.content = content;
        this.memoryId = memoryId;
        this.parentId = parentId;
    }
}
