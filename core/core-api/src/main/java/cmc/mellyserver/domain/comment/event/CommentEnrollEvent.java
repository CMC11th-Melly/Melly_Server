package cmc.mellyserver.domain.comment.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentEnrollEvent {

    private Long memoryId;

    private Long commentWriterId;

}
