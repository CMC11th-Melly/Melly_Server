package cmc.mellyserver.mellycore.comment.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentLikeEvent {

    private Long userId;

    private Long memoryId;

    private String nickname;
}
