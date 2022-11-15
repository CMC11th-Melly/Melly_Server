package cmc.mellyserver.comment.presentation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentUpdateRequest {

    private String content;
}
