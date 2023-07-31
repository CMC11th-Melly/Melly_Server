package cmc.mellyserver.mellycore.comment.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupEvent {

    private Long userId;
}
