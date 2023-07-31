package cmc.mellyserver.mellycore.comment.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentEnrollEvent {

    private Long userId;

    private String nickname;
}
