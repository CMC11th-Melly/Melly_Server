package cmc.mellyserver.comment.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    private String content;
    private Long memoryId;
    @Nullable
    private Long parentId;
    @Nullable
    private Long mentionUserId;
}
