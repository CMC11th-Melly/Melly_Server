package cmc.mellyserver.mellycore.infrastructure.message.fcm;

import cmc.mellyserver.mellycore.notification.application.MessageService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.mellycore.common.constants.FCMConstants.PREFIX_FCMTOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMSendService implements MessageService {

    private final FCMTokenManageService tokenManageService;


    @Override
    public void sendCommentCreatedMessage(String email, String nickname) {

        if (!hasKey(PREFIX_FCMTOKEN + email)) {
            return;
        }

        String token = tokenManageService.getToken(email);
        Message message = Message.builder()
                .putData("title", "댓글 등록")
                .putData("content", nickname + "님이 메모리에 댓글을 남겼습니다.")
                .setToken(token)
                .build();

        send(message);


    }

    @Override
    public void sendCommentLikeCreatedMessage(String email, String nickname) {

        if (!hasKey(PREFIX_FCMTOKEN + email)) {
            return;
        }

        String token = tokenManageService.getToken(email);
        Message message = Message.builder()
                .putData("title", "댓글 좋아요 알림")
                .putData("content", "메모리에 댓글에 좋아요가 달렸습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    public void send(Message message) {
        FirebaseMessaging.getInstance().sendAsync(message);
    }

    public boolean hasKey(String email) {
        return tokenManageService.hasKey(email);
    }
}
