package cmc.mellyserver.domain.comment.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupEvent {

    private Long userId;

}
