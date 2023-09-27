package cmc.mellyserver.mellyapi.domain.comment.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentEnrollEvent {

    private Long userId;

    private Long memoryId;

    private String nickname;
}
