package cmc.mellyserver.comment.presentation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {

    private String content;
    private Long memoryId;
    private Long parentId;
}
