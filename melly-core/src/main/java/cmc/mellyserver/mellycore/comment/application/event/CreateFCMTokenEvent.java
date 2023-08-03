package cmc.mellyserver.mellycore.comment.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateFCMTokenEvent {

    private Long userId;

    private String fcmToken;
}
