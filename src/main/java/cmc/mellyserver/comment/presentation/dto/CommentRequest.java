package cmc.mellyserver.comment.presentation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class CommentRequest {

    private String content;
    private Long memoryId;
    @Nullable
    private Long parentId;
    @Nullable
    private Long mentionUserId;
}
